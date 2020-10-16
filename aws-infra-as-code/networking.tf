# [+] VPC
resource "aws_vpc" "fiap_stock_vpc" {
    cidr_block = lookup(var.map_of_cidr_blocks, "vpc")
    enable_dns_hostnames = true

    tags = {
        Name = "Fiap Stock VPC"
    }
}
# [-]

# [+] INTERNET GATEWAY
resource "aws_internet_gateway" "fiap_stock_internet_gateway" {
    vpc_id = aws_vpc.fiap_stock_vpc.id

    tags = {
        Name = "Fiap Stock Internet Gateway"
    }
}
# [-]

# [+] ELASTIC IP FOR NAT GATEWAY DESIRED ON PUBLIC SUBNET
resource "aws_eip" "fiap_stock_elastic_ip_for_nat_gateway_on_public_subnet" {
    vpc = true
    depends_on = [ aws_internet_gateway.fiap_stock_internet_gateway ]

    tags = {
        Name = "Fiap Stock Elastic IP for NAT Gateway on Public Subnet"
    }
}
# [-]

# [+] PUBLIC SUBNET + NACL + ROUTE TABLE + ROUTE TABLE ASSOCIATION + NAT GATEWAY
resource "aws_subnet" "fiap_stock_public_subnet" {
    vpc_id = aws_vpc.fiap_stock_vpc.id
    cidr_block = lookup(var.map_of_cidr_blocks, "public_subnet")
    map_public_ip_on_launch = true

    tags = {
        Name = "Fiap Stock Public Subnet"
    }
}

resource "aws_network_acl" "fiap_stock_public_nacl" {
    vpc_id = aws_vpc.fiap_stock_vpc.id

    subnet_ids = [ aws_subnet.fiap_stock_public_subnet.id ]

    egress {
        rule_no = 100
        protocol = "-1"
        from_port = 0
        to_port = 0
        cidr_block = "0.0.0.0/0"
        action = "allow"
    }

    ingress {
        rule_no = 100
        protocol = "-1"
        from_port = 0
        to_port = 0
        cidr_block = "0.0.0.0/0"
        action = "allow"
    }

    tags = {
        Name = "Fiap Stock Public Network ACL"
    }
}

resource "aws_route_table" "fiap_stock_public_route_table" {
    vpc_id = aws_vpc.fiap_stock_vpc.id

    route {
        cidr_block = "0.0.0.0/0"
        gateway_id = aws_internet_gateway.fiap_stock_internet_gateway.id
    }

    tags = {
        Name = "Fiap Stock Public Route Table"
    }
}

resource "aws_route_table_association" "fiap_stock_public_route_table_association" {
    route_table_id = aws_route_table.fiap_stock_public_route_table.id
    subnet_id = aws_subnet.fiap_stock_public_subnet.id
}

resource "aws_nat_gateway" "fiap_stock_nat_gateway_on_public_subnet" {
    allocation_id = aws_eip.fiap_stock_elastic_ip_for_nat_gateway_on_public_subnet.id
    subnet_id = aws_subnet.fiap_stock_public_subnet.id
}
# [-]

# [+] PRIVATE SUBNET + NACL + ROUTE TABLE + ROUTE TABLE ASSOCIATION
resource "aws_subnet" "fiap_stock_private_subnet" {
    vpc_id = aws_vpc.fiap_stock_vpc.id
    cidr_block = lookup(var.map_of_cidr_blocks, "private_subnet")
    map_public_ip_on_launch = false

    tags = {
        Name = "Fiap Stock Private Subnet"
    }
}

resource "aws_network_acl" "fiap_stock_private_nacl" {
    vpc_id = aws_vpc.fiap_stock_vpc.id

    subnet_ids = [ aws_subnet.fiap_stock_private_subnet.id ]

    egress {
        rule_no = 100
        protocol = "-1"
        from_port = 0
        to_port = 0
        cidr_block = "0.0.0.0/0"
        action = "allow"
    }

    ingress {
        rule_no = 100
        protocol = "-1"
        from_port = 0
        to_port = 0
        cidr_block = "0.0.0.0/0"
        action = "allow"
    }

    tags = {
        Name = "Fiap Stock Private Network ACL"
    }
}

resource "aws_route_table" "fiap_stock_private_route_table" {
    vpc_id = aws_vpc.fiap_stock_vpc.id

    tags = {
        Name = "Fiap Stock Private Route Table"
    }
}

resource "aws_route_table_association" "fiap_stock_private_route_table_association" {
    route_table_id = aws_route_table.fiap_stock_private_route_table.id
    subnet_id = aws_subnet.fiap_stock_private_subnet.id
}
# [-]

# [+] VPC SECURITY GROUP
resource "aws_security_group" "fiap_stock_security_group" {
    name = "fiap_stock_security_group"
    description = "Fiap Stock Security Group"

    vpc_id = aws_vpc.fiap_stock_vpc.id
    
    ingress {
        from_port   = 0
        to_port     = 65353
        protocol    = "tcp"
        cidr_blocks = ["0.0.0.0/0"]
    }
    
    ingress {
        from_port   = 0
        to_port     = 65353
        protocol    = "udp"
        cidr_blocks = ["0.0.0.0/0"]
    }

    egress {
        from_port   = 0
        to_port     = 0
        protocol    = "-1"
        cidr_blocks = ["0.0.0.0/0"]
    }

    tags = {
        Name = "Fiap Stock Security Group"
    }
}
# [-]