<?php
include_once("connect.php");


$username =$_POST["user_name"];
$passwd=$_POST["passwd"];
 
$query="SELECT * FROM user_auth WHERE name='".$username."' and passwd='".$passwd."'";

$result = mysqli_query($conn,$query);

if($result->num_rows > 0){
    echo "Success,";
	while($row=$result->fetch_array(MYSQLI_NUM))
    {
		foreach($row as $val)
		echo $val." ,  ";
		echo "; ";
		
	}
	exit; 
}
else {
    {echo "Wrong username or password";}
    exit;
}



?>