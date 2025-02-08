# Build stage
FROM node:18 as build
WORKDIR /app
COPY . .
RUN npm install
COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine
COPY --from=build /app/dist/coreui-free-angular-admin-template/browser/* /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/default.conf
