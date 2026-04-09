# Build stage
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Production stage
FROM node:20-slim
WORKDIR /app
COPY package.json ./
RUN npm install --production
COPY --from=build /app/dist ./dist
COPY --from=build /app/server.js ./
# We will use environment variables in Cloud Run for TMDB_API_KEY
EXPOSE 8080
CMD ["node", "server.js"]
