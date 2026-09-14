@echo off
cd /d "%~dp0"
if not defined DB_PASSWORD (
    echo Configure DB_PASSWORD com a senha do PostgreSQL antes de iniciar.
    echo Consulte as instrucoes no README.md.
    pause
    exit /b 1
)
echo Iniciando a API de clientes em http://localhost:8080/clientes/listar-clientes
echo Aguarde a mensagem Started Application. Para encerrar, pressione Ctrl+C.
call mvnw.cmd spring-boot:run
pause
