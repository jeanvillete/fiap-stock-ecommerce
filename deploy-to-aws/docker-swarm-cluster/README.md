# inicializa infra-estrutura
            $ terraform init
            $ terraform apply -auto-approve

## observar *output*, e obter IP/DNS do Nó Lider (Leader Node) que será nosso Manager e Lider

# acessa o nó lider
            $ ssh -i ~/.ssh/fiap_stock_key ubuntu@IP-OU-DNS-PUBLICO-DO-LIDER

# inicializa docker cluster
            $ docker swarm init

## observar saída, e guardar o *join-token* para workers, se possível, pegar todo o comando

# acessar cada um dos workers e executar o comando para se juntar ao cluster
            $ ssh -i ~/.ssh/fiap_stock_key ubuntu@IP-OU-DNS-PUBLICO-DE-CADA-WORKER
            $ docker swarm join --token TOKEN-OBTIDO-DO-LIDER XXX.XXX.XXX.XXX:XXXX