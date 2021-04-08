
var requiredFieldIDs = ["userName", "password", "confirmPassword", "firstName",
"lastName", "maritalStatus", "gender", "dateOfBirth", "address1", "city", "state", "zipCode", "phone","email"];

var optionalFieldIDs = ["address2"];


function resetElements(){
  //reeset required fields
  var i;
  gender = document.getElementsByName("gender");
  maritalStatus = document.getElementsByName("maritalStatus");


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

function markRadio(elementName){
  var element = document.getElementsByName(elementName);
  var isChecked = false;

  for(var i = 0; i < element.length; i++){
    if(element[i].checked){
      isChecked = true;
      break;
    }
  }

  if(isChecked){
    hideElement(elementName + "Error");
  }
  else{
    showElement(elementName + "Error");
  }
}


function showElement(elementName){
  var element = document.getElementById(elementName);

  if(element.classList.contains("hide")){
    element.classList.remove("hide");
  }


}

function hideElement(elementName){
  var element = document.getElementById(elementName);

  if(!element.classList.contains("hide")){
    element.classList.add("hide");
  }


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

//Updates the error states on each item
function updateErrors(errorList){
  errorList.forEach(function(item){
    if(item == "gender" || item == "maritalStatus"){
      markRadio(item);
      return;
    }
    else{
      markInvalidElement(document.getElementById(item));
      showElement(item + "Error");
    }
  });

  requiredFieldIDs.forEach(function(item) {
      if(!errorList.includes(item)){
        if(item == "gender" || item == "maritalStatus"){
          markRadio(item);
          return;
        }
        else{
          markValidElement(document.getElementById(item));
          hideElement(item + "Error");
      }
    }
  });

}

//Submit form
async function sendForm(){
  var form = document.getElementById("registrationForm");
  var data = new FormData(form);

  let response = await fetch('./validateForm.php',{
    method: 'POST',
    body: data
  });

  let result = await response.json();

  if(result.status == "ERROR"){
    updateErrors(result.error);
  }
  else{
    window.location.replace("./confirmation.php?id=" + result.id);
  }

}

function init(){
  document.getElementById("submit").addEventListener("click", function(){
      sendForm();

  });
}

init();
