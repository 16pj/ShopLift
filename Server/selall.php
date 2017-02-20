<?php
require "me.php";
$mysql_qry = "SELECT * FROM `TABLE_ONE` WHERE `Age` = 0;";
$result = mysqli_query($conn, $mysql_qry);
$results = array();
 
 while($row = mysqli_fetch_array($result)){
 echo $row['Name'];
echo "\n";
 }

$conn->close();

?>