# [+] REGION
variable "aws_region" {
    description = "AWS educate default region"
    default = "us-east-1"
}
# [-]

# [+] Amazon/Virtual Machine Image
variable "aws_amis" {
    default = {
        # us-east-1 = "ami-07ebfd5b3428b6f4d"
        us-east-1 = "ami-0dba2cb6798deb6d8"
    }
}
# [-]

# [+] EC2 INSTANCE TYPE
variable "instance_type" {
    default = "t2.micro"
}
# [-]

# [+] MAP NUMBER OF NODES
variable "cluster_no_of_node" {
    type = map
    default = {
        manager = 1
        worker = 1
    }
}
# [-]

# [+] GENERAL PURPOSE MAPS
variable "map_of_cidr_blocks" {
    type = map
    default = {
        vpc = "10.1.0.0/16"
        public_subnet = "10.1.1.0/24"
        private_subnet = "10.1.2.0/24"
    }
}

variable "map_of_instances_volume_sizes" {
    type = map
    default = {
        short = 8
        medium = 16
        large = 32
    }
}
# [-]