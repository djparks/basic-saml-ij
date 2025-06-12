# SAML Certificates

Place your Identity Provider (IdP) certificates in this directory.

For example:
- idp-certificate.crt

These certificates will be used for verifying SAML responses from your Identity Provider.

Note: In a production environment, never commit actual certificates to version control.
Consider using environment variables or a secure vault for managing certificates in production.