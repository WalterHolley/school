<!DOCTYPE html>
<?php
  session_id($_GET['id']);
  session_start();
 ?>
<html lang="en-us">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="./styles/bootstrap.min.css">
    <link rel="stylesheet" href="./styles/index.css">
    <link rel="stylesheet" href="./styles/registration.css">
    <title>Project 1 for CMPSCI3010 - Registration</title>
  </head>
  <body>
  <div id="pageBody" class="container-fluid">
          <div id="header" class="row">
            <p class="h3">Web Based Beginnings: Computer Science 3010</p>
            </div>
        <div id="content" class="row content">
        <div id="menu" class="col-xs-12 col-sm-12 col-md-3 col-lg-2">
          <p><a href="index.html">Home</a></p>
          <p><a href="animation.html">Animations</a></p>
          <p><a href="registration.html">Registration</a></p>
          </div>
            <div id="body" class="col-xs-12 col-sm-12 col-md-9 col-lg-10">
              <div><p class="h5">Registration Confirmation</p></div>
              <form id="registrationForm" class="row">
            <div id="content1" class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <p class="h5">User Information</p>
                <p><ul>
                  <li>User Name:&nbsp;<?php echo $_SESSION['userName'] ?></li>
                  <li>First Name:&nbsp;<?php echo $_SESSION['firstName'] ?></li>
                  <li>Last Name:&nbsp;<?php echo $_SESSION['lastName'] ?></li>
                  <li>Gender:&nbsp;<?php echo $_SESSION['gender'] ?></li>
                  <li>Marital Status:&nbsp;<?php echo $_SESSION['maritalStatus'] ?></li>
                  <li>Date of Birth:&nbsp;<?php echo $_SESSION['dateOfBirth'] ?></li>
                </ul></p>
              </div>
                <div id="content2" class="col-xs-12 col-sm-12 col-md-12 col-lg-6">
                <p class="h5">Contact Information</p>
                <p><ul>
                  <li>Address 1:&nbsp;<?php echo $_SESSION['address1'] ?></li>
                  <li>Address 2:&nbsp;<?php
                  if(isset($_SESSION['address2'])){
                    echo $_SESSION['address2'];
                  } ?>
                  </li>
                  <li>City:&nbsp;<?php echo $_SESSION['city'] ?></li>
                  <li>State:&nbsp;<?php echo $_SESSION['state'] ?></li>
                  <li>Zip Code:&nbsp;<?php echo $_SESSION['zipCode'] ?></li>
                  <li>Phone Number:&nbsp;<?php echo $_SESSION['phone'] ?></li>
                  <li>Email:&nbsp;<?php echo $_SESSION['email'] ?></li>
                </ul></p>

        </div>
        <div id="footer" class="row">
          <div id="links1" class="col-xs-12 col-sm-12 col-md-4">
            <p class="h6">Developer Documentation</p>
            <p><small><a href="https://docs.microsoft.com/en-us/dotnet/csharp/" target="_blank">Microsoft C# Documentation</a></small></p>
            <p><small><a href="https://getbootstrap.com/docs/4.0/getting-started/introduction/" target="_blank">Bootstrap 4 Documentation</a></small></p>
            <p><small><a href="https://W3Schools.com" target="_blank">W3Schools</a></small></p>
          </div>
          <div id="links2" class="col-xs-12 col-sm-12 col-md-4">
            <p class="h6">Personal Interests</p>
            <p><small><a href="https://news.ycombinator.com/" target="_blank">HackerNews</a></small></p>
            <p><small><a href="https://github.com/walterholley" target="_blank">Github Repository</a></small></p>
            <p><small><a href="https://www.guiltygear.com/" target="_blank">Guilty Gear</a></small></p>
          </div>
          <div id="links3" class="col-xs-12 col-sm-12 col-md-4">
            <p class="h6">Developer Tools</p>
            <p><small><a href="https://atom.io/" target="_blank">Atom</a></small></p>
            <p><small><a href="https://www.mamp.info/" target="_blank">MAMP</a></small></p>
            <p><small><a href="https://github.com/" target="_blank">Github</a></small></p>
          </div>
          </div>
      </div>
  </body>
</html>
</!DOCTYPE html>
<?php session_destroy() ?>
