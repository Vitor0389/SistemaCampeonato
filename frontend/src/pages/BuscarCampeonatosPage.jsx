import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function BuscarCampeonatosPage() {
    const [campeonatos, setCampeonatos] = useState([]);
    const [error, setError] = useState('');
    const navigate = useNavigate();

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

    const verDetalhes = (id) => {
        navigate(`/detalhes-campeonato/${id}`);
    };

    return (
        <div style={{ maxWidth: '600px', margin: 'auto' }}>
            <h2>Meus Campeonatos</h2>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {campeonatos.length === 0 && !error && <p>Você ainda não tem campeonatos registrados.</p>}
            <ul style={{ listStyle: 'none', paddingLeft: 0 }}>
                {campeonatos.map(campeonato => (
                    <li key={campeonato.id} style={{ marginBottom: '10px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <span>
                            <strong>{campeonato.name}</strong> (ID: {campeonato.id})
                        </span>
                        <button onClick={() => verDetalhes(campeonato.id)}>Ver Detalhes</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

