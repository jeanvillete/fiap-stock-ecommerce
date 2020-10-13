data "template_file" "based_on_debian_setup" {
    template = file("${path.module}/scripts/setup.sh")
}