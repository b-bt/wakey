## CPU
Os principais usos de CPU da nossa aplicação se resumem em: 
1. Um BroadcastReceiver
2. O custo adicional do S.O. checar pelos Geofences
3. Atualização de localização

Como todos esses usos dependem da implementação especifica do sistema operacional, não há muito que pode ser feito para otimizar o uso da CPU. A funcionalidade aonde pudemos fazer escolhas que impactariam no uso de CPU foi a do geofence em si, onde se recomenda aumentar a área de alcance e se utilizar da flag DWELL, no lugar de ENTER, para não exigir uma atualização de localização com frequência acima do padrão, mas como nossa aplicação é location-critical e essas mudanças são de baixo impacto num App que usa geofence numa escala reduzida e focada, essas recomendações não foram seguidas.

<!-- Inserir imagem exemplo -->

### Boas Práticas
#### Threads
Não foram utilizadas threads enssa aplicação que não fossem de uso implicito definido pela API padrão do Android (Criação de eventos, funcionamento dos Listeners, etc).

#### Evitar APIs deprecadas
Únicas APIs utilizadas nessa aplicação foram as de desenvolvimento padrão do Google e dos bundles do Android, todas em suas versões mais recentes, exceto em casos de compatibilidade com Devices que ainda não estão no Android Q

#### Uso correto de variáveis e constantes
Sempre que possível, foram utilizados valores no formato "val" ou "const" para que o compilador pudesse fazer otimizações de fluxo de código.
