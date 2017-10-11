#delete group
echo group id to delete

read groupId

aws ec2 delete-security-group --group-id $groupId

