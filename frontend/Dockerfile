# Build (Vite)
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Serve (Apache httpd)
FROM httpd:2.4

# Activar módulos necesarios
RUN sed -i 's/^#LoadModule rewrite_module/LoadModule rewrite_module/' /usr/local/apache2/conf/httpd.conf \
 && sed -i 's/^#LoadModule proxy_module/LoadModule proxy_module/' /usr/local/apache2/conf/httpd.conf \
 && sed -i 's/^#LoadModule proxy_http_module/LoadModule proxy_http_module/' /usr/local/apache2/conf/httpd.conf

# Permitir reglas de rewrite
RUN sed -i 's/AllowOverride None/AllowOverride All/' /usr/local/apache2/conf/httpd.conf

# Copiar el build de Vite
COPY --from=build /app/dist/ /usr/local/apache2/htdocs/

# Config extra (SPA + proxy /api)
COPY apache-spa.conf /usr/local/apache2/conf/extra/apache-spa.conf
RUN echo '\nInclude conf/extra/apache-spa.conf\n' >> /usr/local/apache2/conf/httpd.conf

EXPOSE 80
