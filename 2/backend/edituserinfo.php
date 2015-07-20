<?php
header("Content-Type: text/html; charset=utf-8");

include 'common.php';
if($_POST) {
    
    //TODO: check for required fields
    if(!$_POST['input_edit_user_name'] || !$_POST['input_edit_user_pass'] || !$_POST['input_edit_user_email']) {
        echo "Check required fields for editing";
        exit();
    }
    
    
    //connect to DB
    doDB();
    
    //get input & clean them
    $safe_username = mysqli_real_escape_string($mysqli, $_POST['input_edit_user_name']);
    //I obsviously dgaf
    $safe_password = mysqli_real_escape_string($mysqli, $_POST['input_edit_user_pass']);
    $safe_email = mysqli_real_escape_string($mysqli, $_POST['input_edit_user_email']);
    
    $user_id = $_POST['input_edit_user_id'];
    
    
    //update record db
    $edit_user_query = "UPDATE User SET username = '" . $safe_username . "', password = '" . $safe_password . "', email = '" . $safe_email . "' where user_id = '". $user_id . "'";
    
    $edit_user_response = mysqli_query($mysqli, $edit_user_query) or die(mysqli_error($mysqli));
    
    //echo records added
    echo "user $user_id has been edited";
    
    mysqli_free_result($edit_user_response);
    mysqli_close($mysqli);
}

?>