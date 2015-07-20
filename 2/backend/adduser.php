<?php
header("Content-Type: text/html; charset=utf-8");
include 'common.php';
if($_POST) {
    
    //TODO: check for required fields
    if(!$_POST['input_add_user_name'] || !$_POST['input_add_user_pass'] || !$_POST['input_add_user_email']) {
        echo "Check required fields";
        exit();
    }
    
    //connect to DB
    doDB();
    
    //get input & clean them
    $safe_username = mysqli_real_escape_string($mysqli, $_POST['input_add_user_name']);
    //I obsviously dgaf
    $safe_password = mysqli_real_escape_string($mysqli, $_POST['input_add_user_pass']);
    $safe_email = mysqli_real_escape_string($mysqli, $_POST['input_add_user_email']);
    
    //insert into db
    $add_user_query = "INSERT INTO User (username, password, email) values ('" . $safe_username . "', '" . $safe_password . "', '" . $safe_email . "')";
    
    $add_user_response = mysqli_query($mysqli, $add_user_query) or die(mysqli_error($mysqli));
    
    //get user ID
    $user_id = mysqli_insert_id($mysqli);
    
    //echo records added
    echo "user $user_id added";
    
    mysqli_free_result($add_user_response);
    mysqli_close($mysqli);
}

?>