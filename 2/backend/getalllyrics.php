<?php
header("Content-Type: application/json; charset=utf-8");

include 'common.php';
doDB();



$get_all_lyrics_query = "SELECT lyric_id, Lyrics.song_id, song_name, format, link_to_file FROM Lyrics join Song WHERE Lyrics.song_id = Song.song_id ORDER BY song_name ASC";
$get_all_lyrics_response = mysqli_query($mysqli, $get_all_lyrics_query) or die(mysqli_error($mysqli));

$lyricsdata = '[';

while($row = mysqli_fetch_assoc($get_all_lyrics_response)) {
    $lyricsdata .= json_encode($row);
    $lyricsdata .=",";
}
mysqli_free_result($get_all_lyrics_response);
mysqli_close($mysqli);

$lyricsdata = rtrim($lyricsdata, ",");
$lyricsdata .= ']';

echo $lyricsdata;

?>