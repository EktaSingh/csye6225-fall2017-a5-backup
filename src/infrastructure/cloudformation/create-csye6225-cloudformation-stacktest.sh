AMI="ami-cd0f5cb6"
INSTANCE_TYPE="t2.micro"
KEYPAIR_NAME="id_rsa"

#get vpc id
vpcid=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

#stack name
echo Enter stack name
read stackName

#get domain name
echo Enter Domain name
read domainName
domainName=${domainName}.csye6225.com
echo $domainName

#get subnet id for user
subnetID1=$(aws ec2 describe-subnets --filters "Name=availability-zone, Values=us-east-1b" --query "Subnets[0].SubnetId" --output text)
subnetID2=$(aws ec2 describe-subnets --filters "Name=availability-zone, Values=us-east-1a" --query "Subnets[0].SubnetId" --output text)

echo $subnetID1
echo $subnetID2

aws ec2 create-security-group --group-name csye6225-webapp --description "my sg" --vpc-id $vpcid
SECURITY_ID=$(aws ec2 describe-security-groups --group-names csye6225-webapp | grep "GroupId" | awk '{print$2}' | sed -e 's/^"//' -e 's/"$//')
echo $SECURITY_ID

aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 22 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 80 --cidr 10.0.2.15/24
aws ec2 authorize-security-group-ingress --group-id $SECURITY_ID --protocol tcp --port 443 --cidr 10.0.2.15/24
echo "Launching EC2 instance"

INSTANCE_ID=$(aws ec2 run-instances --image-id $AMI --count 1 --instance-type $INSTANCE_TYPE --key-name $KEYPAIR_NAME --security-group-ids $SECURITY_ID --region us-east-1 | grep InstanceId | awk '{print$2}' | tr -cd '[:alnum:]\n-')
echo $INSTANCE_ID

echo "Getting the public IP of the instance"
PUBLIC_IP=$(aws ec2 describe-instances --instance-ids $INSTANCE_ID | grep PublicIpAddress | awk '{print$2}' | tr -cd '[:alnum:]\n.')
echo $PUBLIC_IP


aws cloudformation create-stack --stack-name $stackName --template-body file:///home/niki/Desktop/GitAssignments/csye6225-fall2017-a5/src/infrastructure/cloudformation/templateBodytest.json --parameters ParameterKey=VpcId,ParameterValue=$vpcid ParameterKey=subnetGroupId1,ParameterValue=$subnetID1 ParameterKey=subnetGroupId2,ParameterValue=$subnetID2 ParameterKey=DBName,ParameterValue=csye6225 ParameterKey=EC2SecurityGroupId,ParameterValue=$SECURITY_ID ParameterKey=bucketName,ParameterValue=$domainName


