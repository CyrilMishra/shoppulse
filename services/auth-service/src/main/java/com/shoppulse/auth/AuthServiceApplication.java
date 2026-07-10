package com.shoppulse.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Issues and validates identity. Build order (Block 1, steps A1-A6):
 *
 *  A1  UserEntity (id, email, passwordHash, Set<Role>) + Spring Data JPA repository.
 *  A2  POST /api/auth/register — hash password with BCrypt (NOT plain SHA-256:
 *      BCrypt is deliberately slow + salted; SHA-256 is fast, so brute-forceable.
 *      Know the difference — classic interview trap. JWTs, however, ARE signed
 *      with HMAC-SHA256 — that's integrity, not password storage.)
 *  A3  POST /api/auth/login — verify credentials, return a signed JWT
 *      (claims: sub, roles, iat, exp ~15min) + a refresh token.
 *  A4  SecurityFilterChain bean: stateless (SessionCreationPolicy.STATELESS),
 *      permit /api/auth/**, everything else authenticated.
 *      Interview: stateless JWT vs server-side session vs Redis-backed session —
 *      trade-offs (revocation! scaling! token size!).
 *  A5  RBAC: roles ADMIN / CUSTOMER; protect an endpoint with @PreAuthorize("hasRole('ADMIN')").
 *  A6  Refresh endpoint + (discussion) how you'd revoke: short expiry + Redis denylist.
 *
 *  V2  Move shoppulse.jwt.secret and the DB password into Vault
 *      (vault kv put secret/auth-service shoppulse.jwt.secret=... )
 *
 * Use constructor injection everywhere. @Autowired on fields is the legacy style —
 * know WHY constructor wins: final fields, testability, fail-fast on missing beans.
 */
@SpringBootApplication
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
