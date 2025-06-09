# Etap budowania aplikacji Angular
FROM node:18-slim AS build

# Ustaw katalog roboczy
WORKDIR /app

# Kopiuj pliki package.json i package-lock.json
COPY package*.json ./

# Zainstaluj zależności
RUN npm install

# Skopiuj cały projekt
COPY . .

# Zbuduj aplikację Angular
RUN npm run build --prod

# Użyj serwera nginx do obsługi aplikacji frontendowej
FROM nginx:alpine
COPY --from=build /app/dist/frontend /usr/share/nginx/html

# W expose udostępniamy port 80
EXPOSE 80
