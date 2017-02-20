<?php

require "me.php";

$user_name = $_POST["username"];

$mysql_qry = "DELETE FROM `TABLE_ONE` WHERE `Name` = '$user_name' AND `Age` = 0;";
$result = mysqli_query($conn, $mysql_qry);

if($result != 'NULL')
	echo "DELETE success";	
else echo "Failed to Delete :( !";

?>