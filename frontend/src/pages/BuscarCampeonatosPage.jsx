import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function BuscarCampeonatosPage() {
    const [campeonatos, setCampeonatos] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchCampeonatos = async () => {
            try {
                const token = localStorage.getItem('token');
                if (!token) {
                    setError('Usuário não autenticado');
                    return;
                }
                const response = await axios.get('http://localhost:8080/api/v1/campeonatos', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setCampeonatos(response.data);
            } catch (err) {
                setError('Erro ao buscar campeonatos');
            }
        };

        fetchCampeonatos();
    }, []);

    return (
        <div style={{ maxWidth: '600px', margin: 'auto' }}>
            <h2>Meus Campeonatos</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {campeonatos.length === 0 && !error && <p>Você ainda não tem campeonatos registrados.</p>}
            <ul>
                {campeonatos.map(campeonato => (
                    <li key={campeonato.id}>
                        <strong>{campeonato.name}</strong> (ID: {campeonato.id})
                    </li>
                ))}
            </ul>
        </div>
    );
}
