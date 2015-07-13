<?php

include 'common.php';
if($_POST) {
    
    //TODO: check for required fields
    if(!$_POST['edit_username'] || !$_POST['edit_phone']) {
        echo "Check required fields for editing";
        exit();
    }
    
    
    //connect to DB
    doDB();
    
    //get input & clean them
    $safe_username = mysqli_real_escape_string($mysqli, $_POST['edit_username']);
    //I obsviously dgaf
    $safe_phone = mysqli_real_escape_string($mysqli, $_POST['edit_phone']);
    
    $user_id = $_POST['input_edit_user_id'];
    
    
    //update record db
    $edit_user_query = "UPDATE Users SET username = '" . $safe_username . "', phonenumber = '" . $safe_phone . "' where user_id = '". $user_id . "'";
    
    $edit_user_response = mysqli_query($mysqli, $edit_user_query) or die(mysqli_error($mysqli));
    
    //echo records added
    echo "user $user_id has been edited";
    
    mysqli_free_result($edit_user_response);
    mysqli_close($mysqli);
}

?>