#!/bin/bash

# insere usuário usra, como estoquista
curl "localhost:8181/login" -d '{"login":"usra","type":"stock"}' -H 'Content-Type: application/json'

# insere usuário usrb, como cliente
curl "localhost:8181/login" -d '{"login":"usrb","type":"customer"}' -H 'Content-Type: application/json'

# insere usuário usrc, como cliente
curl "localhost:8181/login" -d '{"login":"usrc","type":"customer"}' -H 'Content-Type: application/json'

# fazendo login e guardando sessão do estoquista usra
usrALoginId=$( curl "localhost:8181/login/stock/usra" )

# estoquista insere itens ao catálogo de produtos do estoque
curl "localhost:8282/stock/users/$usrALoginId/catalogs" -d '{"description": "Laranja - Pacote"}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs" -d '{"description": "Laranja - A granel"}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs" -d '{"description": "Morango - Caixa"}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs" -d '{"description": "Melão - Caixa"}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs" -d '{"description": "Melão - A granel"}' -H 'Content-Type: application/json'

# estoquista adiciona produtos no estoque
curl "localhost:8282/stock/users/$usrALoginId/catalogs/1/products" -d '{"price": 72.63, "quantity": 24}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs/2/products" -d '{"price": 6.32, "quantity": 100}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs/3/products" -d '{"price": 25.11, "quantity": 100}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs/4/products" -d '{"price": 33.23, "quantity": 150}' -H 'Content-Type: application/json'
curl "localhost:8282/stock/users/$usrALoginId/catalogs/5/products" -d '{"price": 3.23, "quantity": 150}' -H 'Content-Type: application/json'

# fazendo login e guardando sessão do cliente usrb
usrBLoginId=$( curl "localhost:8181/login/customer/usrb" )

# fazendo login e guardando sessão do cliente usrc
usrCLoginId=$( curl "localhost:8181/login/customer/usrc" )

# apresenta lista de produtos para cliente usrb
curl "localhost:8383/portal/users/$usrBLoginId/products"
# apresenta detalhes do produto "PRD-0000001" para cliente usrb
curl "localhost:8383/portal/users/$usrBLoginId/products/PRD-0000001"
# apresenta detalhes do produto "PRD-0000002" para cliente usrb
curl "localhost:8383/portal/users/$usrBLoginId/products/PRD-0000002"
# adiciona endereço para o cliente usrb
curl "localhost:8383/portal/users/$usrBLoginId/addresses" -d '{ "zip_code": "12345-678", "complement": "Cond Azul, Bl A Apt 123", "city": "São Paulo", "state": "São Paulo", "country": "Brasil" }' -H 'Content-Type: application/json'
# adiciona pedido para o produto "PRD-0000001" para o cliente usrb
curl "localhost:8383/portal/users/$usrBLoginId/orders" -d '{ "products": [ { "code": "PRD-0000001", "quantity": 10 }, { "code": "PRD-0000002", "quantity": 5 } ], "address": { "zip_code": "12345-678", "complement": "Cond Azul, Bl A Apt 123", "city": "São Paulo", "state": "São Paulo", "country": "Brasil" } }' -H 'Content-Type: application/json'
# cliente usrb lista detalhes de seus pedidos já efetuados, o estado do pedido neste momento é "aguardando resposta"
curl "localhost:8383/portal/users/$usrBLoginId/orders"

# apresenta lista de produtos para cliente usrc
curl "localhost:8383/portal/users/$usrCLoginId/products"
# apresenta detalhes do produto "PRD-0000002" para cliente usrc
curl "localhost:8383/portal/users/$usrCLoginId/products/PRD-0000002"
# apresenta detalhes do produto "PRD-0000003" para cliente usrc
curl "localhost:8383/portal/users/$usrCLoginId/products/PRD-0000003"
# adiciona endereço para o cliente usrc
curl "localhost:8383/portal/users/$usrCLoginId/addresses" -d '{ "zip_code": "54321-876", "complement": "Rua Santos, Nr 49", "city": "Sorocaba", "state": "São Paulo", "country": "Brasil" }' -H 'Content-Type: application/json'
# adiciona pedido para o produto "PRD-0000001" para o cliente usrc
curl "localhost:8383/portal/users/$usrCLoginId/orders" -d '{ "products": [ { "code": "PRD-0000001", "quantity": 10 }, { "code": "PRD-0000002", "quantity": 5 } ], "address": { "zip_code": "54321-876", "complement": "Rua Santos, Nr 49", "city": "Sorocaba", "state": "São Paulo", "country": "Brasil" } }' -H 'Content-Type: application/json'
# cliente usrc lista detalhes de seus pedidos já efetuados, o estado do pedido neste momento é "aguardando resposta"
curl "localhost:8383/portal/users/$usrCLoginId/orders"

# estoquista apresenta pedidos no estoque, onde ambos estão no estado "aguardando resposta"
curl "localhost:8282/stock/users/$usrALoginId/orders"

# estoquista aprova o pedido "ORD-0000001"
curl "localhost:8282/stock/users/$usrALoginId/orders/ORD-0000001/approve" -X PUT

# estoquista reprova o pedido "ORD-0000002"
curl "localhost:8282/stock/users/$usrALoginId/orders/ORD-0000002/reject" -X PUT

# cliente usrb lista detalhes de seus pedidos já efetuados, o estado do pedido neste momento é "aprovado"
curl "localhost:8383/portal/users/$usrBLoginId/orders"

# cliente usrc lista detalhes de seus pedidos já efetuados, o estado do pedido neste momento é "rejeitado"
curl "localhost:8383/portal/users/$usrCLoginId/orders"

# a quantidade no portal é subtraída do pedido aprovado com sucesso, logo neste ponto, mesmo o usuário usra (estoquista) olhando estoque disponível via portal, há quantidade 14 para "PRD-0000001" (antes 24) e 95 para "PRD-0000002" (antes 100)
curl "localhost:8383/portal/users/$usrALoginId/products/PRD-0000001"
curl "localhost:8383/portal/users/$usrALoginId/products/PRD-0000002"