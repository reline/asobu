<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';

doDB();

if(!isset($_GET['song_id']) && !isset($_GET['name']) && !isset($_GET['artist']) && !isset($_GET['album']) && !isset($_GET['genre']) && !isset($_GET['length']) && !isset($_GET['diff']) && !isset($_GET['artwork']) && !isset($_GET['orderby']) && !isset($_GET['desc']) && !isset($_GET['limit'])){
    echo "SONG - look at the url and reevaluate your life";
    exit();
}

$safe_song_id = "";
$safe_song_name = "";
$safe_song_artist = "";
$safe_song_album = "";
$safe_song_genre = "";
$safe_song_length = "";
$safe_song_diff = "";
$safe_album_artwork = "";
$safe_orderby = "";
$safe_ascdesc = 0;
$safe_return_limit = "";

if(isset($_GET['song_id'])){
    $safe_song_id = mysqli_real_escape_string($mysqli, $_GET['song_id']);
    #echo "---------- song_id set: $safe_song_id ----------";
}
if(isset($_GET['name'])){
    $safe_song_name = mysqli_real_escape_string($mysqli, $_GET['name']);
    #echo "---------- song name set: $safe_song_name ----------";
}
if(isset($_GET['artist'])){
    $safe_song_artist = mysqli_real_escape_string($mysqli, $_GET['artist']);
    #echo "---------- song artist set: $safe_song_artist ----------";
}
if(isset($_GET['album'])){
    $safe_song_album = mysqli_real_escape_string($mysqli, $_GET['album']);
    #echo "---------- song album set: $safe_song_album ----------";
}
if(isset($_GET['genre'])){
    $safe_song_genre = mysqli_real_escape_string($mysqli, $_GET['genre']);
    #echo "---------- song genre set: $safe_song_genre ----------";
}
if(isset($_GET['length'])){
    $safe_song_length = mysqli_real_escape_string($mysqli, $_GET['length']);
    #echo "---------- song length set: $safe_song_length ----------";
}
if(isset($_GET['diff'])){
    $safe_song_diff = mysqli_real_escape_string($mysqli, $_GET['diff']);
    #echo "---------- song diff set: $safe_song_diff ----------";
}
if(isset($_GET['artwork'])){
    $safe_album_artwork = mysqli_real_escape_string($mysqli, $_GET['artwork']);
    #echo "---------- album artwork set: $safe_album_artwork ----------";
}
if(isset($_GET['orderby'])){
    $safe_orderby = mysqli_real_escape_string($mysqli, $_GET['orderby']);
    #echo "---------- orderby set: $safe_orderby ----------";
}
if(isset($_GET['desc'])) {
    $safe_desc = mysqli_real_escape_string($mysqli, $_GET['desc']);
    #echo "---------- desc set: $safe_desc ----------";
}
if(isset($_GET['limit'])) {
    $safe_return_limit = mysqli_real_escape_string($mysqli, $_GET['limit']);
    #echo "---------- limit set: $safe_return_limit ----------";
}

if(!$safe_song_id && !$safe_song_name && !$safe_song_artist && !$safe_song_album && !$safe_song_genre && !$safe_song_length && !$safe_song_diff  && !$safe_album_artwork){
    echo "SONG - bruh you're not even searching for anything...";
    exit();
}

if(!$safe_song_id && !$safe_song_name && !$safe_song_artist && !$safe_song_album && !$safe_song_genre && !$safe_song_length && !$safe_song_diff  && !$safe_album_artwork && $safe_return_limit){
    echo "SONG - - bruh I'm gonna need more than that...";
    exit();
}

if(!$safe_song_id && !$safe_song_name && !$safe_song_artist && !$safe_song_album && !$safe_song_genre && !$safe_song_length && !$safe_song_diff  && !$safe_album_artwork && $safe_orderby){
    echo "SONG - - bruh I'm gonna need more than that...";
    exit();
}

if(!$safe_song_id && !$safe_song_name && !$safe_song_artist && !$safe_song_album && !$safe_song_genre && !$safe_song_length && !$safe_song_diff  && !$safe_album_artwork && $safe_desc){
    echo "SONG - - bruh I'm gonna need more than that...";
    exit();
}

$get_song_info_query = "SELECT song_id, song_name, song_artist, song_album, song_genre, song_length, song_diff, album_artwork FROM Song WHERE";

$query_string = "";

if($query_string && $safe_song_id){
    $query_string .= " AND song_id='". $safe_song_id ."'";
}
else if(!$query_string && $safe_song_id){
    $query_string .= " song_id='". $safe_song_id ."'";
}

if($query_string && $safe_song_name){
    $query_string .= " AND song_name LIKE '%". $safe_song_name ."%'";
}
else if(!$query_string && $safe_song_name){
    $query_string .= " song_name LIKE '%". $safe_song_name ."%'";
}

if($query_string && $safe_song_artist){
    $query_string .= " AND song_artist LIKE '%". $safe_song_artist ."%'";
}
else if(!$query_string && $safe_song_artist){
    $query_string .= " song_artist LIKE '%". $safe_song_artist ."%'";
}

if($query_string && $safe_song_album){
    $query_string .= " AND song_album LIKE '%". $safe_song_album ."%'";
}
else if(!$query_string && $safe_song_album){
    $query_string .= " song_album LIKE '%". $safe_song_album ."%'";
}

if($query_string && $safe_song_genre){
    $query_string .= " AND song_genre LIKE '%". $safe_song_genre ."%'";
}
else if(!$query_string && $safe_song_genre){
    $query_string .= " song_genre LIKE '%". $safe_song_genre ."%'";
}

if($query_string && $safe_song_length){
    $query_string .= " AND song_length='". $safe_song_length ."'";
}
else if(!$query_string && $safe_song_length){
    $query_string .= " song_length='". $safe_song_length ."'";
}

if($query_string && $safe_song_diff){
    $query_string .= " AND song_diff='". $safe_song_diff ."'";
}
else if(!$query_string && $safe_song_diff){
    $query_string .= " song_diff='". $safe_song_diff ."'";
}

if($query_string && $safe_album_artwork){
    $query_string .= " AND album_artwork='". $safe_album_artwork ."'";
}
else if(!$query_string && $safe_album_artwork){
    $query_string .= " album_artwork='". $safe_album_artwork ."'";
}

if($query_string && $safe_orderby){
    $query_string .= " ORDER BY " . $safe_orderby;
    /*
    if($safe_desc && ($safe_desc === 1)){
        $query_string .= " DESC";
    }
    */
}

if($query_string && $safe_return_limit){
    $query_string .= " LIMIT " . $safe_return_limit;
}

#echo "querystring: " . $query_string . "------------------------------------------------------------";
$get_song_info_query .= $query_string;
#echo "query: " . $get_song_info_query . "------------------------------------------------------------";

$get_song_info_response = mysqli_query($mysqli, $get_song_info_query) or die(mysqli_error($mysqli));


$songifo = "[";

while($row = mysqli_fetch_assoc($get_song_info_response)){
    $songifo .= json_encode($row);
    $songifo .= "," ;
}
                                                           
$songifo = rtrim($songifo, ",");
$songifo .= "]";

echo $songifo;

mysqli_free_result($get_song_info_response);
mysqli_close($mysqli);

?>