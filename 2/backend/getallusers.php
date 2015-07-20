<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';
doDB();

$get_all_users_query = "SELECT user_id, username, password, email FROM User";
$sql_response = mysqli_query($mysqli, $get_all_users_query) or die(mysqli_error($mysqli));

$userdata = '[';
while($row = mysqli_fetch_assoc($sql_response)){
    $userdata .= json_encode($row);
    $userdata .= ",";
}
mysqli_free_result($sql_response);
mysqli_close($mysqli);

$userdata = trim($userdata, ",");
$userdata .= "]";
echo $userdata;
?>