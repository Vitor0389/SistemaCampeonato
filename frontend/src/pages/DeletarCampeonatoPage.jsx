import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function DeletarCampeonatoPage() {
    const [campeonatos, setCampeonatos] = useState([]);
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');

    const token = localStorage.getItem('token');

    useEffect(() => {
        carregarCampeonatos();
    }, []);

    const carregarCampeonatos = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/v1/campeonatos', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setCampeonatos(response.data);
        } catch (err) {
            setErro('Erro ao buscar campeonatos.');
        }
    };

    const deletarCampeonato = async (id) => {
        setErro('');
        setSucesso('');
        try {
            await axios.delete(`http://localhost:8080/api/v1/campeonatos/${id}`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            setSucesso('Campeonato deletado com sucesso!');
            carregarCampeonatos();
        } catch (err) {
            setErro('Erro ao deletar campeonato.');
        }
    };

    return (
        <div style={{maxWidth: '600px', margin: 'auto'}}>
            <h2>Deletar Campeonatos</h2>
            {campeonatos.length === 0 ? (
                <p>Você ainda não tem campeonatos cadastrados.</p>
            ) : (
                <ul>
                    {campeonatos.map((camp) => (
                        <li key={camp.id} style={{marginBottom: '10px'}}>
                            {camp.name}{' '}
                            <button onClick={() => deletarCampeonato(camp.id)}>Deletar</button>
                        </li>
                    ))}
                </ul>
            )}
            {erro && <p style={{color: 'red'}}>{erro}</p>}
            {sucesso && <p style={{color: 'green'}}>{sucesso}</p>}
        </div>
    );
}