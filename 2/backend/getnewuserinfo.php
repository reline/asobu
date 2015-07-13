<?php
//get the information of a spesific user
header("Content-Type: application/json");

include 'common.php';


if(!isset($_GET['user_id']) && !isset($_GET['username']) && !isset($_GET['phone']) && !isset($_GET['limit'])){
    echo "USER - look at the url and reevaluate your life";
    exit();
}

/*
if($_GET['user_id'] == -1) {
    exit();
}
*/

doDB();

$safe_user_id = "";
$safe_username = "";
$safe_userphone = "";
$safe_return_limit = "";

if(isset($_GET['user_id'])){
    $safe_user_id = mysqli_real_escape_string($mysqli, $_GET['user_id']);
}
if(isset($_GET['username'])){
    $safe_username = mysqli_real_escape_string($mysqli, $_GET['username']);
}
if(isset($_GET['phone'])){
    $safe_userphone = mysqli_real_escape_string($mysqli, $_GET['phone']);
}
if(isset($_GET['limit'])){
    $safe_return_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
}

if(!$safe_user_id && !$safe_username && !$safe_userphone && !$safe_return_limit){
    echo "USER - bruh you're not even searching for anything...";
    exit();
}

/*
$get_user_info_query = "SELECT user_id, username, phonenumber from Users where user_id = " . $safe_user_id;*/

$get_user_info_query = "SELECT user_id, username, phonenumber FROM Users WHERE";

$query_string = "";

if($query_string && $safe_user_id){
    $query_string .= " AND user_id='" . $safe_user_id . "'";
}
else if(!$query_string && $safe_user_id) {
    $query_string .= " user_id='" . $safe_user_id . "'";
}

if($query_string && $safe_username){
    $query_string .= " AND username LIKE '%" . $safe_username . "%'";
}
else if(!$query_string && $safe_username) {
    $query_string .= " username LIKE '%" . $safe_username . "%'";
}

if($query_string && $safe_userphone){
    $query_string .= " AND phonenumber LIKE '%" . $safe_userphone . "%'";
}
else if(!$query_string && $safe_userphone) {
    $query_string .= " phonenumber LIKE '%" . $safe_userphone . "%'";
}

if($query_string && $safe_return_limit){
    $query_string .= " LIMIT " . $safe_return_limit;
}

$get_user_info_query .= $query_string;

//echo "query----------------" . $get_user_info_query;

$get_user_info_response = mysqli_query($mysqli, $get_user_info_query) or die(mysqli_error($mysqli));

$userifo = "[";

while($row = mysqli_fetch_assoc($get_user_info_response)){
    $userifo .= json_encode($row);
    $userifo .= "," ;
}
                                                           
$userifo = rtrim($userifo, ",");
$userifo .= "]";
echo $userifo;

mysqli_free_result($get_user_info_response);
mysqli_close($mysqli);


?>