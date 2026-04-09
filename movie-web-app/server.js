import express from 'express';
import path from 'path';
import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const app = express();
const PORT = process.env.PORT || 8080;
const ACCESS_TOKEN = process.env.TMDB_ACCESS_TOKEN;
const BASE_URL = 'https://api.themoviedb.org/3';

const fetchOptions = {
    method: 'GET',
    headers: {
        accept: 'application/json',
        Authorization: `Bearer ${ACCESS_TOKEN}`
    }
};

app.use(express.static(path.join(__dirname, 'dist')));

// Proxy endpoint for movies
app.get('/api/movies/:type', async (req, res) => {
    const { type } = req.params; // e.g., 'popular', 'top_rated', 'upcoming'
    try {
        const response = await fetch(`${BASE_URL}/movie/${type}`, fetchOptions);
        const data = await response.json();
        res.json(data);
    } catch (error) {
        console.error('Error fetching movies:', error);
        res.status(500).json({ error: 'Failed to fetch movies' });
    }
});

// Proxy endpoint for movie details
app.get('/api/movie/:id', async (req, res) => {
    const { id } = req.params;
    try {
        const response = await fetch(`${BASE_URL}/movie/${id}?append_to_response=videos,credits`, fetchOptions);
        const data = await response.json();
        res.json(data);
    } catch (error) {
        console.error('Error fetching movie details:', error);
        res.status(500).json({ error: 'Failed to fetch movie details' });
    }
});

// Search endpoint
app.get('/api/search', async (req, res) => {
    const { query } = req.query;
    try {
        const response = await fetch(`${BASE_URL}/search/movie?query=${encodeURIComponent(query)}`, fetchOptions);
        const data = await response.json();
        res.json(data);
    } catch (error) {
        console.error('Error searching movies:', error);
        res.status(500).json({ error: 'Failed to search movies' });
    }
});

app.use((req, res) => {
    console.log(`Serving index.html for ${req.url}`);
    res.sendFile(path.join(__dirname, 'dist', 'index.html'));
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Server is running on port ${PORT}`);
});
