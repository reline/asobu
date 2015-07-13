<?php
header("Content-Type: application/json");

include 'common.php';

doDB();


//...I'm not proud to say I don't know why && works but || doesn't...oh well
//TODO: find out why I guess
if(!isset($_GET['media_id']) && !isset($_GET['song_id']) && !isset($_GET['type']) && !isset($_GET['limit'])){
    echo "MEDIA - look at the url and reevaluate your life";
    exit();
}

$safe_media_id = "";
$safe_song_id = "";
$safe_media_type = "";
$safe_return_limit = "";

if(isset($_GET['media_id'])){
    $safe_media_id = mysqli_real_escape_string($mysqli, $_GET['media_id']);
}
if(isset($_GET['song_id'])){
    $safe_song_id = mysqli_real_escape_string($mysqli, $_GET['song_id']);
}
if(isset($_GET['type'])){
    $safe_media_type = mysqli_real_escape_string($mysqli, $_GET['type']);
}
if(isset($_GET['limit'])){
    $safe_return_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
}



if(!$safe_media_id && !$safe_song_id && !$safe_media_type && !$safe_return_limit){
    echo "MEDIA - bruh you're not even searching for anything...";
    exit();
}
/*
else if(!$safe_media_id && !$safe_song_id ) {
    echo "MEDIA - almost there...pls provide an id";
    exit();
}
*/
if(!$safe_media_id && !$safe_song_id && !$safe_media_type && $safe_return_limit){
    echo "MEDIA - bruh I'm gonna need more than that...";
    exit();
}

$get_media_info_query = "SELECT media_id, Media.song_id, song_name, type_of_link, link FROM Media join Song where Media.song_id = Song.song_id AND";

$query_string = "";

/*
if($safe_media_id && !$safe_song_id) {
    $get_media_info_query = "SELECT media_id, Media.song_id, song_name, type_of_link, link FROM Media join Song where media_id = '" . $safe_media_id . "' AND Media.song_id = Song.song_id";
    
}
else if(!$safe_media_id && $safe_song_id) {
    $get_media_info_query = "SELECT media_id, Media.song_id, song_name, type_of_link, link FROM Media join Song where Media.song_id = '" . $safe_song_id . "' AND Media.song_id = Song.song_id";
}
else if($safe_media_id && $safe_song_id) {
    $get_media_info_query = "SELECT media_id, Media.song_id, song_name, type_of_link, link FROM Media join Song where media_id = '" . $safe_media_id. "' AND Media.song_id = '" . $safe_song_id . "' AND Media.song_id = Song.song_id";
    
}
*/

if($query_string && $safe_media_id){
    $query_string .= " AND media_id='" . $safe_media_id . "'";
}
else if(!$query_string && $safe_media_id){
    $query_string .= " media_id='" . $safe_media_id . "'";
}

if($query_string && $safe_song_id){
    $query_string .= " AND Media.song_id='" . $safe_song_id . "'";
}
else if(!$query_string && $safe_song_id){
    $query_string .= " Media.song_id='" . $safe_song_id . "'";
}

if($query_string && $safe_media_type){
    $query_string .= " AND type_of_link='" . $safe_media_type . "'";
}
else if(!$query_string && $safe_media_type){
    $query_string .= " type_of_link='" . $safe_media_type . "'";
}

if($safe_return_limit){
    $query_string .= " LIMIT " . $safe_return_limit;
}

$get_media_info_query .= $query_string;

$get_media_info_response = mysqli_query($mysqli, $get_media_info_query) or die(mysqli_error($mysqli));



$mediaifo = "[";

while($row = mysqli_fetch_assoc($get_media_info_response)){
    $mediaifo .= json_encode($row);
    $mediaifo .= "," ;
}
                                                           
$mediaifo = rtrim($mediaifo, ",");
$mediaifo .= "]";
echo $mediaifo;

mysqli_free_result($get_media_info_response);
mysqli_close($mysqli);

?>