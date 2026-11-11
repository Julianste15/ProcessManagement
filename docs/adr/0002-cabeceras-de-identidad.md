# ADR-0002: Defensa en profundidad para las cabeceras de identidad

- **Estado:** Aceptada
- **Fecha:** 2026-11-10
- **Contexto del cambio:** issue #36
- **Decisores:** equipo de desarrollo

## Contexto

El API Gateway valida el JWT y, a partir de sus claims, inyecta las cabeceras
`X-User-Email`, `X-User-Role` y `X-User-IsDepartmentHead`. Los microservicios de
dominio confían en esas cabeceras para aplicar sus reglas de autorización.

El problema original era doble:

1. `docker-compose.yml` publicaba los puertos internos de los servicios, así que
   cualquiera podía saltarse el gateway y enviar las cabeceras `X-User-*` a mano.
2. El gateway no eliminaba las cabeceras que el cliente enviara, de modo que un
   cliente podía intentar inyectarlas y confundir a los servicios.

## Decisión

Se aplica **defensa en profundidad** con dos capas complementarias:

1. **Saneamiento en el gateway.** `AuthenticationFilter` elimina
   `X-User-Email`, `X-User-Role` y `X-User-IsDepartmentHead` de *toda* petición
   entrante (incluidos los endpoints públicos) y solo las reconstruye a partir
   del JWT validado.
2. **Puertos internos cerrados.** Docker Compose publica únicamente `8080`
   (gateway), `8761` (Eureka) y `15672` (consola de RabbitMQ). Los servicios de
   dominio solo son alcanzables dentro de la red `microservices-network`.

## Justificación

- **No se confía en la red como única defensa.** Cerrar los puertos es
  necesario, pero no suficiente: un despliegue mal configurado o un acceso
  lateral dentro del clúster volvería a exponer los servicios.
- **El gateway es el único lugar autorizado para crear identidad.** Si el
  cliente envía `X-User-Role: COORDINATOR`, esa cabecera se descarta antes de
  llegar a cualquier servicio.
- **Los servicios de dominio siguen revalidando la regla fina** (por ejemplo,
  solo `COORDINATOR` evalúa un Formato A). Ver ADR-0001 y issue #57.
- **Es verificable con pruebas unitarias** del filtro, sin depender de Docker.

## Alternativas consideradas

1. **Firmar las cabeceras con un secreto interno.** Añade criptografía y rotación
   de claves para un beneficio marginal frente al saneamiento; se descarta por
   complejidad.
2. **Validar el JWT en cada servicio.** Es la opción más fuerte, pero exige
   compartir la clave o usar JWKS y duplica la lógica de autenticación. Queda
   como evolución posible si el sistema crece.
3. **Solo cerrar los puertos.** Rechazada: protege ante un atacante externo,
   pero no ante un cliente que ya está dentro de la red o ante un despliegue
   incorrecto.

## Consecuencias

### Positivas

- Las cabeceras forjadas nunca llegan a un servicio de dominio.
- El modelo de autorización depende del JWT, no de la buena fe del cliente.
- La superficie de red expuesta se reduce a tres puertos.

### Negativas / trade-offs

- Los servicios de dominio no son accesibles directamente para depuración; hay
  que usar el gateway o `docker compose exec`.
- Si en el futuro se añade otro punto de entrada, debe replicar el saneamiento.

## Verificación

- `AuthenticationFilterTest` cubre:
  - cabecera forjada en endpoint protegido (el filtro la descarta y usa el JWT);
  - cabeceras `X-User-*` enviadas a un endpoint público (también se descartan).
- `docker-compose.yml` no publica puertos de servicios de dominio (validado con
  un parser YAML en el pipeline local).

## Referencias

- Issue #36: *Cerrar el bypass de autenticación por cabeceras X-User-*.
- Issue #57: *La aprobación del Formato A no validaba el rol*.
- `gateway-service/src/main/java/co/unicauca/gateway/filters/AuthenticationFilter.java`
- `docker-compose.yml`
