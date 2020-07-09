# southsystem-importer-api

API responsável por receber os arquivos, salvar no S3 e enviar para processamento usando mensageria.
 - É possível enviar mais de um arquivo por vez
 - Os arquivos precisam ter a extensão .dat
 - É possível verificar o status do arquivo e se este foi processado por completo recuperar o arquivo com o sumário pedido. O postman com os endpoints se encontram no projeto
 - Foi realizado deploy de um pod da API em um cluster na Digital Ocean

Melhorias sugeridas nessa API
 - Cobertura de testes usando mockito, rest assured
 - Uso do Spring Security com JWT. Com isso poderíamos integrar com outros sistemas, cada um tendo um client_id e secret para que os mesmos pudessem enviar seus arquivos e que só poderiam ser manipulados por eles mesmos. Com isso também teríamos flexibilidade para ativar e desativar clientes
 
