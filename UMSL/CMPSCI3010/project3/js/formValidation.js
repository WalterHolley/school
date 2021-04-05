
var regex = null;
var requiredFieldIDs = ["userName", "password", "confirmPassword", "firstName",
"lastName", "dateOfBirth", "address1", "city", "state", "zipCode", "phone","email"];

var optionalFieldIDs = ["address2"];

function validateContentSize(element, minSize, maxSize){

  var result = false;
  if(element.value.length <= maxSize && element.value.length >= minSize)
    result = true;

  return result;
}

function resetElements(){
  //reeset required fields
  var i;
  gender = document.getElementsByName("gender");
  maritalStatus = document.getElementsByName("maritalStatus");

  //disable Submit
  document.getElementById("submit").disabled = true;

  //clear required fields
  for(i = 0; i < requiredFieldIDs.length; i++){
    element = document.getElementById(requiredFieldIDs[i]);
    element.value = '';
    element.classList.remove("is-valid");
    element.classList.remove("is-invalid");
  }

  //clear gender radio button
  for(i = 0; i < gender.length; i++){
    gender[i].checked = false;
  }

  //clear marital status radio button
  for(i = 0; i < maritalStatus.length; i++){
    maritalStatus[i].checked = false;
  }

}
//Ensures a state value is selected
function validateState(){
  result = false;
  stateList = document.getElementById("state");

  if(stateList.options[stateList.selectedIndex].value !== null ||
    !stateList.options[stateList.selectedIndex].value === ""){
    result = true;
  }
  return result;
}

//validate phone number
function validatePhone(element){
  result = false;
  let phoneRegex1 = /[0-9]{10}/;
  let phoneRegex2 = /[0-9]{3}-[0-9]{3}-[0-9]{4}/;
  length = element.value.length;


if(length == 10 || length == 12){
  regex = new RegExp(phoneRegex1);
  if(regex.test(element.value)){
    result = true;
    element.value = element.value.slice(0,3) + "-" + element.value.slice(3,6) + "-" + element.value.slice(6,11);
  }else{
    regex = new RegExp(phoneRegex2);
    if(regex.test(element.value)){
      result = true;
      element.value = element.value.substring(0,12);
    }
  }
}

  return result;

}

function validateZipCode(element){
  result = false;
  let zipCodeRegex = /(^\d{5}-\d{4})|(^\d{9})|(^\d{5})/;
  let zipCodeRegexNineDigit = /(^\d{9})/;

  length = element.value.length;
  regex = new RegExp(zipCodeRegex);

  if(regex.test(element.value)){

    if(length == 10 || length == 5){
      result = true;
    }else if(length == 9 && zipCodeRegexNineDigit.test(element.value)){
      result = true;
      element.value = element.value.slice(0,5) + "-" + element.value.slice(5,9);
    }

  }

  return result;
}

function validatePassword(passwordValue){
  result = false;
  let lowercase = /[a-z]/;
  let upperCase = /[A-Z]/;
  let digit = /[0-9]/;
  let special = /[^a-zA-Z0-9]/;

  regex = new RegExp(lowercase);
    //contains one lowercase
  if(regex.test(passwordValue)){
    regex = new RegExp(special);
    //contains one special
    if(regex.test(passwordValue)){
      regex = new RegExp(upperCase);
      //contains one uppercase
      if(regex.test(passwordValue)){
        regex = new RegExp(digit);
        //contains one digit
        if(regex.test(passwordValue)){
          result = true;
        }
      }
    }
  }

  return result;
}

function validateEmail(element){
  let emailRegex = /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
  result = false;

  if(emailRegex.test(element.value)){
    result = true;
  }

  return result;
}

function validateDOB(element){
  result = false;
  let dateRegex = /(^\d{4}-\d{2}-\d{2})/;
  var currentDate = new Date();

  if(dateRegex.test(element.value) && element.value.length == 10){
    var selectedDate = new Date(element.value);
    if(selectedDate < currentDate){
      result = true;
    }
  }
  return result;
}

function showElement(elementName){
  document.getElementById(elementName).classList.remove("hide");

}

function hideElement(elementName){
  document.getElementById(elementName).classList.add("hide");

}

function markValidElement(element){
  //remove invalid class
  element.classList.remove("is-invalid");

  //add valid class
  element.classList.add("is-valid");
}

function markInvalidElement(element){
  //remove valid class
  element.classList.remove("is-valid");

  //remove invalid class
  element.classList.add("is-invalid");

}

//determines of the radio buttons have been selected,
//manages their error states
function validateRadioButtons(){
  var result = false;
  gender = document.getElementsByName("gender");
  maritalStatus = document.getElementsByName("maritalStatus");
  genderError = document.getElementById("genderError");
  statusError = document.getElementById("statusError");
  genderSelected = false;
  statusSelected = false;
  var i;

  for(i = 0; i < gender.length; i++){
    if(gender[i].checked){
      genderSelected = true;
      break;
    }
  }

  for(i = 0; i < maritalStatus.length; i++){
    if(maritalStatus[i].checked){
      statusSelected = true;
      break;
    }
  }



  if(!genderSelected){
    showElement("genderError");
  }else{
    hideElement("genderError");
  }

  if(!statusSelected){
    showElement("statusError");
  }else{
    hideElement("statusError");
  }

  if(genderSelected && statusSelected){
    result = true;
  }

  return result;
}

//determines of a radio button has a selected option
function validateRadioButton(elementCollection){
  var result = false;

  for(i = 0; i < elementCollection.length; i++){
    if(elementCollection[i].checked){
      result = true;
      break;
    }
  }

  return result;
}

//validates the form
function formIsValid(){
  result = false;
  //check required and optional fields
  if(validateRequiredFields() && !document.getElementById("address2").classList.contains("is-invalid")){
    result = true;
    document.getElementById("submit").disabled = false;
  }else{
    document.getElementById("submit").disabled = true;
  }

  return result;
}

//determines if all required fields have been properly populated
function validateRequiredFields(){
  var result = true;
  gender = document.getElementsByName("gender");
  maritalStatus = document.getElementsByName("maritalStatus");
  var i;
  for(i = 0; i < requiredFieldIDs.length; i++){
    if(!document.getElementById(requiredFieldIDs[i]).classList.contains("is-valid")){
      result = false;
    }
  }

  if(result){
    result = validateRadioButton(gender);
    result = validateRadioButton(maritalStatus);
  }

  return result;
}

function validateOptionalFields(){
  var i;
  result = true;

  for(i = 0; i < optionalFieldIDs.length; i++){
    if(document.getElementById(optionalFieldIDs[i]).classList.contains("is-invalid")){
      result = false;
    }
  }
}
//initialize form
function init(){
  //disable Submit
  document.getElementById("submit").disabled = true;
  var i;
  maritalStatus = document.getElementsByName("maritalStatus");
  gender = document.getElementsByName("gender");

  //set event listeners
  document.getElementById("userName").addEventListener("change", function(){
      var element = document.getElementById("userName");
      if(!validateContentSize(element,6, 50)){
        markInvalidElement(element);
        showElement("userNameError");
      }else{
        markValidElement(element);
        hideElement("userNameError");
      }
      formIsValid();
  });

  document.getElementById("password").addEventListener("change", function(){
    var element = document.getElementById("password");
    if(!validateContentSize(element,8, 50) || !validatePassword(element.value)){
      markInvalidElement(element);
      showElement("passwordError");
    }else{
      markValidElement(element);
      hideElement("passwordError");
    }
    formIsValid();
  });

  document.getElementById("confirmPassword").addEventListener("change", function(){
    var element = document.getElementById("confirmPassword");
    originalPassword = document.getElementById("password").value;
    if(!validateContentSize(element, 8, 50) || !validatePassword(element.value) ||
     element.value !== originalPassword){
      markInvalidElement(element);
      showElement("confirmPasswordError");
    }else{
      markValidElement(element);
      hideElement("confirmPasswordError");
    }
    formIsValid();
  });

  document.getElementById("firstName").addEventListener("change", function(){
    var element = document.getElementById("firstName");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("firstNameError");
    }else{
      markValidElement(element);
      hideElement("firstNameError");
    }
    formIsValid();
  });

  document.getElementById("lastName").addEventListener("change", function(){
    var element = document.getElementById("lastName");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("lastNameError");
    }else{
      markValidElement(element);
      hideElement("lastNameError");
    }
    formIsValid();
  });

  document.getElementById("address1").addEventListener("change", function(){
    var element = document.getElementById("address1");
    if(!validateContentSize(element, 1, 100)){
      markInvalidElement(element);
      showElement("address1Error");
    }else{
      markValidElement(element);
      hideElement("address1Error");
    }
    formIsValid();
  });

  document.getElementById("address2").addEventListener("change", function(){
    var element = document.getElementById("address2");
    if(!validateContentSize(element, 1, 100)){
      markInvalidElement(element);
      showElement("address2Error");
    }else{
      markValidElement(element);
      hideElement("address2Error");
    }
    formIsValid();
  });

  document.getElementById("city").addEventListener("change", function(){
    var element = document.getElementById("city");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("cityError");
    }else{
      markValidElement(element);
      hideElement("cityError");
    }
      formIsValid();
  });

  document.getElementById("state").addEventListener("change", function(){
    var element = document.getElementById("state");
    if(!validateState()){
      markInvalidElement(element);
      showElement("stateError");
    }else{
      markValidElement(element);
      hideElement("stateError");
    }
    formIsValid();
  });

  document.getElementById("zipCode").addEventListener("change", function(){
    var element = document.getElementById("zipCode");
    if(!validateZipCode(element)){
      markInvalidElement(element);
      showElement("zipCodeError");
    }else{
      markValidElement(element);
      hideElement("zipCodeError");
    }
    formIsValid();
  });

  document.getElementById("phone").addEventListener("change", function(){
    var element = document.getElementById("phone");
    if(!validatePhone(element)){
      markInvalidElement(element);
      showElement("phoneError");
    }else{
      markValidElement(element);
      hideElement("phoneError");
    }
    formIsValid();
  });

  document.getElementById("email").addEventListener("change", function(){
    var element = document.getElementById("email");
    if(!validateEmail(element)){
      markInvalidElement(element);
      showElement("emailError");
    }else{
      markValidElement(element);
      hideElement("emailError");
    }
    formIsValid();
  });

  document.getElementById("dateOfBirth").addEventListener("change", function(){
    var element = document.getElementById("dateOfBirth");
    if(!validateDOB(element)){
      markInvalidElement(element);
      showElement("dateOfBirthError");
    }else{
      markValidElement(element);
      hideElement("dateOfBirthError");
    }
    formIsValid();
  });

  document.getElementById("reset").addEventListener("click", function(){
      resetElements();
  });

  document.getElementById("submit").addEventListener("click", function(event){
    event.preventDefault();
      sendForm();

  });

  //event listeners for radio buttons
  for(i = 0; i < gender.length; i++){
    gender[i].addEventListener("change", function(){
      if(validateRadioButton(gender)){
        showElement("genderError");
      }else{
        hideElement("genderError");
      }
    });
  }

  for(i = 0; i < maritalStatus.length; i++){
    maritalStatus[i].addEventListener("change", function(){
      if(validateRadioButton(maritalStatus)){
        showElement("statusError");
        formIsValid()
      }else{
        hideElement("statusError");
        formIsValid();
      }
    });
  }

}

async function sendForm(){
  var form = document.getElementById("registrationForm");
  var data = new FormData(form);

  let response = await fetch('./validateForm.php',{
    method: 'POST',
    body: data
  });

  let result = await response.json();

  if(result.status == "ERROR"){

  }
  else{
    window.location.replace("./confirmation.php?id=" + result.id);
  }

}

init();
