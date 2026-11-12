# Histórico público saneado

| Estado | Commit experimental original | Commit público saneado |
|---|---|---|
| Base | `4bf9fffdea92bac3a5a7f19cab9714fd2e3f88a3` | `15b2215acfb08d77402ac4a41f0be358ca4e5afd` |
| Atual | `b1690656f1d896d23c44276dcb5c0764d3e50186` | este commit |

Todos os arquivos Java e POM foram preservados byte a byte. Arquivos PEM foram excluídos, padrões `*.pem`/`*.key` foram ignorados e os valores padrão de configuração de banco e caminhos de chaves JWT foram substituídos por variáveis de ambiente. O histórico experimental original permanece no repositório local e não foi enviado ao repositório público. Para reproduzir métricas dos commits-base originais, use os SHAs experimentais locais; esta publicação saneada tem SHAs diferentes.
