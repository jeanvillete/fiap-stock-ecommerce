# [+] KEY PAIR; PUBLIC KEY
resource "aws_key_pair" "fiap_stock_key_pair" {
    key_name = "fiap_stock_key_name"
    public_key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCzVYFdD1uM78kGWneLDp5d/NtCwLeDhXD/enTd1JY82ZOtGkMT2AoxV4g64RlnNiW4vAdBVW8tK3c5g3sZmqtJRxr7tdoGUtf+WxaEDXOqnHuIsh3ykwe0W4gCZ0k+wZgvucaIh5WK/MGZcw/piXgeaSS2ilu6MtVG+a2TMzIEOcNXDrsHGtjKFAjJ69LZh8szEL8vdXBsQJldA31uU96mXDfcICcmPeVC19jK2CEjDKJqLG7E/Fd/uwPpOBqSVVPPM9qJZ/93gaK4T+VqaUnWIOvHjPAx50eAZA2DAPBsVFbejRxJJh5O2QYfKU2IY5zMsRiXrL17Lx37+J6rsAUn jean@thinkpad"

    tags = {
        Name = "Fiap Stock Key Pair"
    }
}
# [-]