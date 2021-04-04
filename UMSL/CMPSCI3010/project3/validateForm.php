<?php

//requied field name list
$required_fields = array("userName", "password", "confirmPassword","firstName", "lastName", "gender",
"maritalStatus", "dateOfBirth","address1", "state", "city", "zipCode", "phone", "email");

$optional_fields = array("address2");
//check for post method
if($_SERVER['REQUEST_METHOD'] === "POST"){
  error_log(print_r($_POST, TRUE));
  echo json_encode(validateForm());
}
//validates length of field
//returns false on invalid length, otherwise true
function validateLength($valueString, $minLength, $maxLength){
  $result = false;
  if(strlen($valueString) >= $minLength && strlen($valueString) <= $maxLength){
    $result = true;
  }

  return $result;
}


//validates password
function isValidPassword($password){
  $lowercase = "/[a-z]/";
  $upperCase = "/[A-Z]/";
  $digit = "/[0-9]/";
  $special = "/[^a-zA-Z0-9]/";
  $isValid = false;

  if(validateLength($password, 8, 50) && (preg_match($lowercase, $password) &&
  preg_match($upperCase, $password) && preg_match($digit, $password) &&
  preg_match($special, $password))){
    $isValid = true;
  }

  return $isValid;
}

//validate confirmPassword
function isValidConfirmPassword($password){
  $isValid = false;
  if(isValidPassword($password) && $password === $_POST['password']){
    $isValid = true;
  }

  return $isValid;
}

//validates email
function isValidEmail($email){
  $emailRegex = "/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/";

  return preg_match($emailRegex, $email);
}

//validates date of Birth
function isValidDateOfBirth($dob){
  $regex = "/(^\d{4}-\d{2}-\d{2})/";
  return preg_match($regex, $dob);
}

//validates phone Number
function isValidPhoneNumber($phoneNumber){
  $isValid = false;
  $phoneRegex1 = "/[0-9]{10}/";
  $phoneRegex2 = "/[0-9]{3}-[0-9]{3}-[0-9]{4}/";

  if(validateLength($fieldValue, 12,12)){
    $isValid = preg_match($phoneRegex1, $phoneNumber);
  }
  elseif(validateLength($fieldValue, 10,10)){
    $isValid = preg_match($phoneRegex2, $phoneNumber);
  }

  return $isValid;
}

//validates ZipCode
function isValidZipCode($zipCode){
  $isValid = false;
  $zipCodeRegex = "/(^\d{5}-\d{4})|(^\d{9})|(^\d{5})/";
  $zipCodeRegexNineDigit = "/(^\d{9})/";
  $length = strlen($zipCode);

  if(($length == 5 || $length == 9 || $length == 10) &&
  (preg_match($zipCodeRegex, $zipCode))){
    $isValid = true;
  }

  return $isValid;

}

//validates required fields
//returns array of fields that are invalid
function validateRequiredFields(){
  $errorFields = array();
    foreach($required_fields as $field){
      $isValid = false;
      $fieldValue = trim($_POST[$field]);

      if($field == "address1"){
        $isValid = validateLength($fieldValue, 1, 100);
      }
      elseif ($field == "state") {
        $isValid = validateLength($fieldValue, 1, 52);
      }
      elseif ($field == "zipCode") {
        if(isValidZipCode($fieldValue)){
          $isValid = true;
        }
      }
      elseif ($field == "phone") {
        if(isValidPhoneNumber($fieldValue)){
          $isValid = true;
        }
      }
      elseif ($field == "password") {
        if(isValidPassword($fieldValue)){
          $isValid = true;
        }
      }
      elseif($field="email"){
        $isValid = isValidEmail($fieldValue);
      }
      elseif($field="dateOfBirth"){
        $isValid = isValidDateOfBirth($fieldValue);
      }
      else {
        if(validateLength($fieldValue, 1, 50)){
          $isValid = true;
        }
      }

      //html encode field value if valid, and update post value
      if($isValid){

      }
      else{
        $errorFields[] = $field;
      }


    }
}

//validates optional fields
function validateOptionalFields(){

}

//sanitizes input from form
function sanitizeInput(){

}

//validates a submitted registration form
function validateForm(){
  $errorFields = array();
  $result = array();
  $errorFields = validateRequiredFields();
  $errorFields[] = validateOptionalFields();

  //when all requirements are met, store values in session, and redirect to confirmation page.
  if(!count($errorFields)){
    $result['status'] = "OK";
  }
  else{
    $result['status'] = "ERROR";
    $result['error'] = $errorFields;
  }

  return $result;

}

 ?>
