#
echo Instance id to kill

read instanceId
aws ec2 terminate-instances --instance-ids $instanceId
