<?php

//requied field name list
$required_fields = array("userName", "password", "confirmPassword","firstName", "lastName", "gender",
"maritalStatus", "dateOfBirth","address1", "state", "city", "zipCode", "phone", "email");

//optional field name list
$optional_fields = array("address2");

$stateList = array(
  "AL" => "Alabama",
	"AK" => "Alaska",
	"AZ" => "Arizona",
	"AR" => "Arkansas",
  "CA" => "California",
	"CO" => "Colorado",
  "CT" => "Connecticut",
	"DE" => "Delaware",
  "DC" => "District Of Columbia",
  "FL" => "Florida",
  "GA" => "Georgia",
  "HI" => "Hawaii",
  "ID" => "Idaho",
  "IL" => "Illinois",
  "IN" => "Indiana",
  "IA" => "Iowa",
  "KS" => "Kansas",
  "KY" => "Kentucky",
  "LA" => "Louisiana",
  "ME" => "Maine",
	"MD" => "Maryland",
  "MA" => "Massachusetts",
	"MI" => "Michigan",
	"MN" => "Minnesota",
	"MS" => "Mississippi",
	"MO" => "Missouri",
	"MT" => "Montana",
	"NE" => "Nebraska",
	"NV" => "Nevada",
	"NH" => "New Hampshire",
	"NJ" => "New Jersey",
	"NM" => "New Mexico",
	"NY" => "New York",
	"NC" => "North Carolina",
	"ND" => "North Dakota",
	"OH" => "Ohio",
	"OK" => "Oklahoma",
	"OR" => "Oregon",
	"PA" => "Pennsylvania",
	"RI" => "Rhode Island",
	"SC" => "South Carolina",
	"SD" => "South Dakota",
	"TN" => "Tennessee",
	"TX" => "Texas",
	"UT" => "Utah",
  "VT" => "Vermont",
	"VA" => "Virginia",
	"WA" => "Washington",
	"WV" => "West Virginia",
	"WI" => "Wisconsin",
	"WY" => "Wyoming");

//check for post method
if($_SERVER['REQUEST_METHOD'] === "POST"){
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
  $isValid = false;
  $regex = "/(^\d{4}-\d{2}-\d{2})/";

  if(preg_match($regex, $dob) && ($dob < date("Y-m-d"))){
    $isValid = true;
    $dateValue = substr($dob,5,2)."/".substr($dob,8,2)."/".substr($dob,0,4);
    $_POST['dateOfBirth'] = $dateValue;
  }
  return $isValid;
}

//validates phone Number
function isValidPhoneNumber($phoneNumber){
  $isValid = false;
  $phoneRegex1 = "/[0-9]{10}/";
  $phoneRegex2 = "/[0-9]{3}-[0-9]{3}-[0-9]{4}/";

  if(validateLength($phoneNumber, 10,10)){
    $isValid = preg_match($phoneRegex1, $phoneNumber);
    if($isValid){
      $phoneNumber = substr($phoneNumber,0,3)."-".substr($phoneNumber,3,3)."-".substr($phoneNumber,7,4);
      $_POST['phone'] = $phoneNumber;
    }
  }
  elseif(validateLength($phoneNumber, 12,12)){
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

    if($length == 9){
      $zipCode = substr($zipCode,0,5)."-".substr($zipCode,5,4);
      $_POST['zipCode'] = $zipCode;
    }
  }

  return $isValid;

}

function isValidState($state){
  $isValid = false;
  global $stateList;

  if(array_key_exists($state, $stateList) && validateLength($state, 1,52)){
    $isValid = true;
  }

  return $isValid;
}

//validates required fields
//returns array of fields that are invalid
function validateRequiredFields(){
  $errorFields = null;
  global $required_fields;
  global $stateList;

    foreach($required_fields as $field){
      $isValid = false;
      if(!isset($_POST[$field])){
        if($errorFields == null){
          $errorFields = array();
        }
        $errorFields[] = $field;
        continue;
      }

      $fieldValue = trim($_POST[$field]);

      if($field == "address1"){
        $isValid = validateLength($fieldValue, 1, 100);
      }
      elseif ($field == "state") {
        $isValid = false;
        if(isValidState($fieldValue)){
          $fieldValue = $stateList[$fieldValue];
          $isValid = true;
        }

      }
      elseif ($field == "zipCode") {
        if(isValidZipCode($fieldValue)){
          $isValid = true;
          $fieldValue = $_POST[$field];
        }
      }
      elseif ($field == "phone") {
        if(isValidPhoneNumber($fieldValue)){
          $isValid = true;
          $fieldValue = $_POST[$field];
        }
      }
      elseif ($field == "password") {
        if(isValidPassword($fieldValue)){
          $isValid = true;
        }
      }
      elseif($field == "confirmPassword"){
        if(isValidConfirmPassword($fieldValue)){
          $isValid = true;
        }
      }
      elseif($field == "email"){
        $isValid = isValidEmail($fieldValue);
      }
      elseif($field == "dateOfBirth"){
        if(isValidDateOfBirth($fieldValue)){
          $fieldValue = $_POST[$field];
          $isValid = true;
        }

      }
      else {
        if(validateLength($fieldValue, 1, 50)){
          $isValid = true;
        }
      }

      //html encode field value if valid, and update post value
      if($isValid){
        $_POST[$field] = sanitizeInput($fieldValue);
      }
      else{
        if($errorFields == null){
          $errorFields = array();
        }
        $errorFields[] = $field;
      }
    }

    return $errorFields;
}

//validates optional fields
function validateOptionalFields(){
  global $optional_fields;
  $errors = null;
  foreach($optional_fields as $field){
    if(isset($_POST[$field])){
      if(!validateLength($_POST[$field],1,100)){
        if($errors == null){
          $errors = array();
        }
        $errors[] = $field;
      }
      else{
        $_POST[$field] = sanitizeInput($_POST[$field]);
      }
    }
  }
  return $errors;
}

//sanitizes input from form
function sanitizeInput($fieldValue){
  return htmlentities($fieldValue);
}

//validates a submitted registration form
function validateForm(){
  $errorFields = null;
  $result = array();
  $errorFields = validateRequiredFields();
  $optionalFields = validateOptionalFields();

  if($optionalFields !== null){
    $errorFields[] = $optionalFields;
  }

  //when all requirements are met, store values in session, and redirect to confirmation page.
  if($errorFields == null){
    session_start();
    $result['status'] = "OK";
    $result['id'] = session_id();
    unset($_POST['password']);
    unset($_POST['confirmPassword']);
    $_SESSION = $_POST;
  }
  else{
    $result['status'] = "ERROR";
    $result['error'] = $errorFields;
  }
  return $result;


}

 ?>
