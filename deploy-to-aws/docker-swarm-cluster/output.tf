output "fiap_stock_managers_output" {
    value = {
        for instance_manager in aws_instance.fiap_stock_ec2_managers:
            instance_manager.id => format("[%s], DNS = [%s], PUBLIC IP = [%s]", instance_manager.tags.Name, "http://${instance_manager.public_dns}", "http://${instance_manager.public_ip}")
    }
}

output "fiap_stock_workers_output" {
    value = {
        for instance_workers in aws_instance.fiap_stock_ec2_workers:
            instance_workers.id => format("[%s], DNS = [%s], PUBLIC IP = [%s]", instance_workers.tags.Name, "http://${instance_workers.public_dns}", "http://${instance_workers.public_ip}")
    }
}