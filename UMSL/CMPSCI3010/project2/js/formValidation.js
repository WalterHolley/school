
var regex = null;
function validateContentSize(element, minSize, maxSize){

  var result = false;
  if(element.value.length <= maxSize && element.value.length >= minSize)
    result = true;

  return result;
}

//Ensures a state value is selected
function validateState(){
  result = false;
  stateList = document.getElementById("state");

  if(stateList.includes(stateList.value)){
    result = true;
  }
  return result;
}

function validatePhone(element){
  result = false;
  let phoneRegex1 = /[0-9]{10}/;
  let phoneRegex2 = /[0-9]{3}-[0-9]{3}-[0-9]{4}/;

  regex = new RegExp(phoneRegex1)

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

  return result;

}

function validateZipCode(element){
  result = false;
  let zipCodeRegex1 = /[0-9]{5}/;
  let zipCodeRegex2 = /[0-9]{5}-[0-9]{4}/;

  regex = new RegExp(zipCodeRegex1);

  if(regex.test(element.value)){

    result = true;
    element.value = element.value.slice(0,5);
  }else{
    regex = new RegExp(zipCodeRegex2);
    if(regex.test(element.value)){
      result = true;
      element.value = element.value.substring(0,10);
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

function validateEmail(emailValue){
  result = false;

  return result;
}

function validateDOB(){
  result = false;

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
//initialize event listeners
function init(){

  document.getElementById("userName").addEventListener("change", function(){
      element = document.getElementById("userName");
      if(!validateContentSize(element,6, 50)){
        markInvalidElement(element);
        showElement("userNameError");
      }else{
        markValidElement(element);
        hideElement("userNameError");
      }
  });

  document.getElementById("password").addEventListener("change", function(){
    element = document.getElementById("password");
    if(!validateContentSize(element,8, 50) || !validatePassword(element.value)){
      markInvalidElement(element);
      showElement("passwordError");
    }else{
      markValidElement(element);
      hideElement("passwordError");
    }
  });

  document.getElementById("confirmPassword").addEventListener("change", function(){
    element = document.getElementById("confirmPassword");
    originalPassword = document.getElementById("password").value;
    if(!validateContentSize(element, 8, 50) || !validatePassword(element.value) ||
    element.value !== originalPassword){
      markInvalidElement(element);
      showElement("confirmPasswordError");
    }else{
      markValidElement(element);
      hideElement("confirmPasswordError");
    }
  });

  document.getElementById("firstName").addEventListener("change", function(){
    element = document.getElementById("firstName");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("firstNameError");
    }else{
      markValidElement(element);
      hideElement("firstNameError");
    }
  });

  document.getElementById("lastName").addEventListener("change", function(){
    element = document.getElementById("lastName");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("lastNameError");
    }else{
      markValidElement(element);
      hideElement("lastNameError");
    }
  });

  document.getElementById("address1").addEventListener("change", function(){
    element = document.getElementById("address1");
    if(!validateContentSize(element, 1, 100)){
      markInvalidElement(element);
      showElement("address1Error");
    }else{
      markValidElement(element);
      hideElement("address1Error");
    }
  });

  document.getElementById("address2").addEventListener("change", function(){
    element = document.getElementById("address2");
    if(!validateContentSize(element, 1, 100)){
      markInvalidElement(element);
      showElement("address2Error");
    }else{
      markValidElement(element);
      hideElement("address2Error");
    }
  });

  document.getElementById("city").addEventListener("change", function(){
    element = document.getElementById("city");
    if(!validateContentSize(element, 1, 50)){
      markInvalidElement(element);
      showElement("cityError");
    }else{
      markValidElement(element);
      hideElement("cityError");
    }
  });

  document.getElementById("state").addEventListener("change", function(){
    element = document.getElementById("state");
    if(!validateState()){
      markInvalidElement(element);
      showElement("stateError");
    }else{
      markValidElement(element);
      hideElement("stateError");
    }
  });

  document.getElementById("zipCode").addEventListener("change", function(){
    element = document.getElementById("zipCode");
    if(!validateContentSize(element, 5,10) || !validateZipCode(element)){
      markInvalidElement(element);
      showElement("zipCodeError");
    }else{
      markValidElement(element);
      hideElement("zipCodeError");
    }
  });

  document.getElementById("phone").addEventListener("change", function(){
    element = document.getElementById("phone");
    if(!validatePhone(element)){
      markInvalidElement(element);
      showElement("phoneError");
    }else{
      markValidElement(element);
      hideElement("phoneError");
    }
  });

  document.getElementById("email").addEventListener("change", function(){
    element = document.getElementById("email");
    if(!validateEmail()){
      markInvalidElement(element);
      showElement("emailError");
    }else{
      markValidElement(element);
      hideElement("emailError");
    }
  });

  document.getElementById("dateOfBirth").addEventListener("change", function(){
    element = document.getElementById("dateOfBirth");
    if(!validateDOB()){
      markInvalidElement(element);
      showElement("dateOfBirthError");
    }else{
      markValidElement(element);
      hideElement("dateOfBirthError");
    }
  });



}



init();
