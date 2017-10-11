#get vpc id
vpcid=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

echo $vpcid

#create stack
echo Enter stack name

read stackName


echo $stackName

#create security group
groupID=$(aws ec2 create-security-group --group-name csye6225-fall2017-$stackName-webapp --description 'AWS EC2 instance security group' --vpc-id $vpcid --query 'GroupId' --output text)

echo $groupID

#configure security group
aws ec2 authorize-security-group-ingress --group-id $groupID --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id $groupID --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id $groupID --protocol tcp --port 22 --cidr 0.0.0.0/0

#get subnet id
subnetID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$vpcid" --query "Subnets[0].SubnetId" --output text)

echo $subnetID

aws cloudformation create-stack --stack-name $stackName --template-body file:///home/niki/Desktop/GitAssignments/csye6225-fall2017-a4/src/infrastructure/cloudformation/templateBody.json --parameters ParameterKey=KeyName,ParameterValue=id_rsa ParameterKey=InstanceType,ParameterValue=t2.micro
