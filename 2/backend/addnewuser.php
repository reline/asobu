<?php
header("Content-Type: text/html; charset=utf-8");
include 'common.php';
if($_POST) {
    
    //TODO: check for required fields
    if(!$_POST['add_username'] || !$_POST['add_phone']) {
        echo "Check required fields";
        exit();
    }
    
    //connect to DB
    doDB();
    
    //get input & clean them
    $safe_username = mysqli_real_escape_string($mysqli, $_POST['add_username']);
    //...do you have to encrypt phone numbers...?
    $safe_phone = mysqli_real_escape_string($mysqli, $_POST['add_phone']);
    
    //insert into db
    $add_user_query = "INSERT INTO Users (username, phonenumber) values ('" . $safe_username . "', '" . $safe_phone . "')";
    
    $add_user_response = mysqli_query($mysqli, $add_user_query) or die(mysqli_error($mysqli));
    
    //get user ID
    $user_id = mysqli_insert_id($mysqli);
    
    //echo records added
    echo "user $user_id added";
    echo '<br><br><a href="../editnewusers.html">BACK</a>';
    
    mysqli_free_result($add_user_response);
    mysqli_close($mysqli);
}

?>