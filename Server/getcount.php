<?php

require "me.php";
$mysql_qry = " SELECT COUNT( `Name` ) AS counter FROM `TABLE_ONE` WHERE `Age` =0 ;";
$result = mysqli_query($conn, $mysql_qry);

echo $result->fetch_object()->counter;


 mysqli_close($conn);


?>
 