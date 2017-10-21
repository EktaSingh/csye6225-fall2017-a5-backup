
echo Enter stack name to delete

read stackName
aws cloudformation delete-stack --stack-name $stackName
