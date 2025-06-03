import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

export default function BuscarCampeonatosPage() {
    const [campeonatos, setCampeonatos] = useState([]);
    const [erro, setErro] = useState('');
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        async function carregarCampeonatos() {
            try {
                const response = await axios.get('http://localhost:8080/api/v1/campeonatos', {
                    headers: { Authorization: `Bearer ${token}` }
                });
                setCampeonatos(response.data);
            } catch (err) {
                setErro('Erro ao buscar campeonatos.');
            }
        }
        carregarCampeonatos();
    }, [token]);

    const verDetalhes = (id) => {
        navigate(`/detalhes-campeonato/${id}`);
    };

    return (
        <div style={{ maxWidth: '600px', margin: 'auto' }}>
            <h2>Campeonatos</h2>
            {campeonatos.length === 0 ? (
                <p>Nenhum campeonato encontrado.</p>
            ) : (
                campeonatos.map(camp => (
                    <div key={camp.id} style={{ marginBottom: '10px' }}>
                        <span>{camp.name}</span>{' '}
                        <button onClick={() => verDetalhes(camp.id)}>Ver Detalhes</button>
                    </div>
                ))
            )}
            {erro && <p style={{ color: 'red' }}>{erro}</p>}
        </div>
    );
}
