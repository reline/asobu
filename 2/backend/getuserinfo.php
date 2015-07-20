<?php
//get the information of a spesific user
header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

$safe_user_id = mysqli_real_escape_string($mysqli, $_GET['user_id']);

$get_user_info_query = "SELECT username, password, email from User where user_id = " . $safe_user_id;
$get_user_info_response = mysqli_query($mysqli, $get_user_info_query) or die(mysqli_error($mysqli));

$userifo = "";

$row = mysqli_fetch_assoc($get_user_info_response);
$userifo = json_encode($row);

echo $userifo;

mysqli_free_result($get_user_info_response);
mysqli_close($mysqli);

?>