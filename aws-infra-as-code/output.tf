output "fiap_stock_managers_output" {
    value = {
        for instance_manager in aws_instance.fiap_stock_ec2_managers:
            instance_manager.id => format("[%s], PUBLIC DNS = [%s], PUBLIC IP = [%s], PRIVATE DNS = [%s], PRIVATE IP = [%s]", instance_manager.tags.Name, instance_manager.public_dns, instance_manager.public_ip, instance_manager.private_dns, instance_manager.private_ip)
    }
}

output "fiap_stock_workers_output" {
    value = {
        for instance_workers in aws_instance.fiap_stock_ec2_workers:
            instance_workers.id => format("[%s], PUBLIC DNS = [%s], PUBLIC IP = [%s], PRIVATE DNS = [%s], PRIVATE IP = [%s]", instance_workers.tags.Name, instance_workers.public_dns, instance_workers.public_ip, instance_workers.private_dns, instance_workers.private_ip)
    }
}