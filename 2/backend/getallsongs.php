<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';
doDB();

$get_all_songs_query = "SELECT song_id, song_name, song_artist, song_album, song_genre, song_length, song_diff, album_artwork FROM Song";
$get_all_songs_response = mysqli_query($mysqli, $get_all_songs_query) or die(mysqli_error($mysqli));

$songdata = '[';

while($row = mysqli_fetch_assoc($get_all_songs_response)) {
    $songdata .= json_encode($row);
    $songdata .=",";
}
mysqli_free_result($get_all_songs_response);
mysqli_close($mysqli);

$songdata = rtrim($songdata, ",");
$songdata .= ']';

echo $songdata;

?>