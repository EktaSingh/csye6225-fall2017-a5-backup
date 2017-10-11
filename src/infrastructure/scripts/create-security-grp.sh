vpcid=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

echo $vpcid

groupId=$(aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description "AWS EC2 instance security group" --vpc-id $vpcid --output text)
echo $groupId

aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 22 --cidr 0.0.0.0/0


