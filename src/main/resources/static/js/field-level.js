function getKnowledgeSection(classifier, level, elementId1, elementId2) {
    let firstLevel = document.getElementById(elementId1).value;
    let secondLevel = document.getElementById(elementId2);
    let requestURL = '';

    switch (level) {
        case 'second':
            requestURL = `get_knowledge_section?classifier=${classifier}&level=second_${firstLevel}`;
            break;
        case 'third':
            requestURL = `get_knowledge_section?classifier=${classifier}&level=third_${firstLevel}`
            break;
    }

    console.log(requestURL);

    fetch(requestURL, { method: 'GET' }).then(async response => {
        if (response.ok) {
            let result = await response.json();

            if (level === 'second') secondLevel.innerHTML = '<option value="none">Выберите значимый раздел данной области</option>'
            else secondLevel.innerHTML = '<option value="none">Выберите значимое направление данного раздела</option>'
            for (let i = 0; i < result.length; i++) {
                secondLevel.innerHTML += `<option value="${result[i]}">${result[i]}</option>`
            }
        } else {
            console.log('ОшибОЧКА');
            if (level === 'second') secondLevel.innerHTML = '<option value="none">Выберите значимый раздел данной области</option>'
            else secondLevel.innerHTML = '<option value="none">Выберите значимое направление данного раздела</option>'
        }
    });

    console.log(firstLevel);
}
