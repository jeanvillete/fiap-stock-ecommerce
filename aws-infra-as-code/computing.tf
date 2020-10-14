# [+] INSTANCES FOR CLUSTER NODES TYPED AS MANAGERS/LEADERS
resource "aws_instance" "fiap_stock_ec2_managers" {
    ami = lookup( var.aws_amis, var.aws_region )
    instance_type = var.instance_type

    subnet_id = aws_subnet.fiap_stock_public_subnet.id
    associate_public_ip_address = true
    vpc_security_group_ids = [ aws_security_group.fiap_stock_security_group.id ]

    root_block_device {
        volume_size = lookup( var.map_of_instances_volume_sizes, "short" )
    }

    key_name = aws_key_pair.fiap_stock_key_pair.key_name

    user_data = data.template_file.based_on_debian_setup.rendered

    count = lookup( var.cluster_no_of_node, "manager" )

    tags = {
        Name = format("Fiap Stock Leader Node No %d", count.index + 1)
    }
}
# [-]

# [+] INSTANCES FOR CLUSTER NODES TYPED AS WORKERS/FOLLOWERS
resource "aws_instance" "fiap_stock_ec2_workers" {
    ami = lookup( var.aws_amis, var.aws_region )
    instance_type = var.instance_type

    subnet_id = aws_subnet.fiap_stock_public_subnet.id
    associate_public_ip_address = true
    vpc_security_group_ids = [ aws_security_group.fiap_stock_security_group.id ]

    root_block_device {
        volume_size = lookup( var.map_of_instances_volume_sizes, "medium" )
    }

    key_name = aws_key_pair.fiap_stock_key_pair.key_name

    user_data = data.template_file.based_on_debian_setup.rendered

    count = lookup( var.cluster_no_of_node, "worker" )

    tags = {
        Name = format("Fiap Stock Follower Node No %d", count.index + 1)
    }
}
# [-]