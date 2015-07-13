<?php
header("Content-Type: application/json");

include 'common.php';
doDB();

$get_all_media_query = "SELECT media_id, Media.song_id, song_name, type_of_link, link FROM Media join Song WHERE Media.song_id = Song.song_id ORDER BY Media.song_id ASC";
$get_all_media_response = mysqli_query($mysqli, $get_all_media_query) or die(mysqli_error($mysqli));

$mediadata = '[';

while($row = mysqli_fetch_assoc($get_all_media_response)) {
    $mediadata .= json_encode($row);
    $mediadata .=",";
}
mysqli_free_result($get_all_media_response);
mysqli_close($mysqli);

$mediadata = rtrim($mediadata, ",");
$mediadata .= ']';

echo $mediadata;

?>