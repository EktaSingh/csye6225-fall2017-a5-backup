#get vpc id
vpcid=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)

echo $vpcid

#create security group
groupID=$(aws ec2 create-security-group --group-name csye6225-fall2017-webapp --description 'AWS EC2 instance security group' --vpc-id $vpcid --query 'GroupId' --output text)

echo $groupID

#configure security group
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 80 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-name csye6225-fall2017-webapp --protocol tcp --port 22 --cidr 0.0.0.0/0

#get subnet id
subnetID=$(aws ec2 describe-subnets --filters "Name=vpc-id, Values=$vpcid" --query "Subnets[0].SubnetId" --output text)

echo $subnetID

#Launch EC2 Instance
instances=$(aws ec2 run-instances --image-id ami-cd0f5cb6 --count 1 --instance-type t2.micro --key-name id_rsa --security-group-ids $groupID --subnet-id $subnetID --block-device-mappings "[{\"DeviceName\":\"/dev/sdf\",\"Ebs\":{\"VolumeSize\":16,\"VolumeType\":\"gp2\",\"DeleteOnTermination\":true}}]" --query 'Instances[0].InstanceId' --output text)

echo "$instances"

#Retrieving instance’s public IP address.
aws ec2 wait instance-running --instance-ids $instances

echo "instance-running"

publicIP=$(aws ec2 describe-instances --instance-ids $instances --query 'Reservations[0].Instances[0].PublicIpAddress' --output text)

echo "$publicIP"

#Add/Update type A resource record set
aws route53 change-resource-record-sets --hosted-zone-id Z1ONDTQ879LS7N --cli-input-json "{ \"ChangeBatch\": {\"Comment\": \"Update the A record set\",\"Changes\": [{\"Action\": \"UPSERT\",\"ResourceRecordSet\": { \"Name\": \"csye6225-fall2017-singhek.me.\",\"Type\": \"A\",\"TTL\": 60,\"ResourceRecords\": [{\"Value\": \"$publicIP\"}]}}]}}"
