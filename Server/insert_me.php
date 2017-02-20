<?php

require "me.php";

$user_name = $_POST[username];
$user_pass = $_POST[password];
$mysql_qry = "INSERT INTO `a1844335_DUST`.`TABLE_ONE` (
`Name` ,
`Age`
)
VALUES (
'$user_name', '$user_pass'
);";
$result = mysqli_query($conn, $mysql_qry);
if($result)
	echo "INSERT success";	
else echo "Failed to insert :( !";

$conn->close();

?>