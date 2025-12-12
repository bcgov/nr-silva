SET client_encoding TO 'UTF8';
SET synchronous_commit TO off;
SET search_path = silva,public;

TRUNCATE TABLE silv_damage_agent_code;

COPY silv_damage_agent_code (silv_damage_agent_code,description,effective_date,expiry_date,update_timestamp) FROM STDIN;
AO	Pika  (L. Ochotona spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFX	Brown Felt Blight  (L. Herpotrichia spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDY	Birch-Aspen Leafroller  (L. Epinotia solandriana )	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NAV	Avalanche or Snow Slide	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFO	Lophodermium Needle Cast  (L. Lophodermium seditiosum)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFY	Hendersonia Needle Cast  (L. Hendersonia pinicola)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFZ	Rhizosphaera Needle Cast  (L. Rhizosphaera kalkhoffii)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFU	Cedar Leaf Blight  (L. Didymascella thujina)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLK	Conifer Cytospora Canker  (L. Leucostoma kunzei)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRS	Schweinitzii Butt Rot  (L. Phaeolus schweinitzii)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NX	Wind Scarring or Rubbing	2009-07-13 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFN	Leptomelanconium Needle Blight  (L. Leptomelanconium pinicola)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFJ	Phaeoseptoria Needle Cast  (L. Phaeoseptoria contortae)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NCY	Yellow Cedar (Yc) Decline	2006-11-06 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NCA	Aspen Decline	2006-11-06 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NCB	Birch Decline	2006-11-06 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID2	Bruce Spanworm  (L. Operophtera bruceata)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID3	Winter Moth  (L. Operophtera brumata)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID4	Cottonwood Sawfly  (L. Nematus currani)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID5	Fall Webworm  (L. Hyphantria cunea)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID7	Woolly Alder Sawfly  (L. Eriocampa ovata)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDK	Northern Tent Caterpillar  (L. Malacosoma californicum)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID6	Aspen Leaf Miner  (L. Phyllocristis populiella)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID8	Aspen Leaf Roller  (L. Pseudexentera oregonana)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID9	Birch Leaf Skeletonizer  (L. Buccalatrix spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEA	Unidentified Aspen Defoliation	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDO	Filament Bearer  (L. Nematocampa fiamentaria)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDJ	Gray Forest Loooper  (L. Caripeta divista)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDQ	Hemlock Needle Miner  (L. Epinotia tsugana)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEB	Hemlock Sawfly  (L. Neodiprion tsugae)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEC	Larch Budmoth  (L. Zairaphera improbana)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IED	Larch Looper  (L. Semiothis sexmaculata)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEF	Cottonwood Leaf Skeletonizer  (L. Phyllonorycytes apparella)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEG	Lodgepole Pine Sawfly  (L. Neodiprion nanulus contortae)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEH	Phantom Hemlock Looper  (L. Nepytia phantasmaria)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEI	Saddleback Looper  (L. Ectropis crepuscularia)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEJ	Willow Leafminer  (L. Micrurapteryx salicifoliella)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFB	Delphinella Tip Blight  (L. Delphinella spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFF	Marssonina Leaf Blights  (L. Marssonina spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFG	Cottonwood Leaf Rust  (L. Melampsora occidentalis)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFI	Linospora Leaf Blotch  (L. Linospora tetraspora)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFK	Septoria Leaf Spot  (L. Septoria populicola)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFT	Sirococcus Tip Blight  (L. Sirococcus conigenus)	2002-12-19 00:00:00	2020-10-08 00:00:00	2022-02-03 14:08:49
DDA	White Mottled Rot  (L. Ganoderma applanatum)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDC	Brown Cubical Rot of Birch  (L. Piptoporus betulinus)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDG	Sterile Conk Trunk Rot of Birch  (L. Inonotus obliquus)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBH	Hylurgops Beetle  (L. Hylurgops rugipennis)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBF	Fir Engraver Beetle  (L. Scolytus ventralis)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBL	Lodgepole Pine Beetle  (L. Dendroctonus murryanae)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBE	Silver Fir Beetle  (L. Pseudohylesinus sericeus)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBR	Fir Root Bark Beetle  (L. Pseudohylesinus granulatus)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISA	Bronze Birch Borer  (L. Agrilus anxius)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISC	Poplar Borer  (L. Saperda calcarata)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISW	Poplar and Willow Borer  (L. Cryptorhynchus lapathi)	2002-12-19 00:00:00	9999-12-30 00:00:00	2022-02-03 14:08:49
ID1	Leaf Beetles  (L. Chrysomela spp.)	2009-12-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
UBT	Unknown Broken Top	2013-04-08 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
UCR	Unknown Crook	2013-04-08 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
USW	Unknown Sweep	2013-04-08 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFQ	Alpine Fir Needle Cast  (L. Isthmiella quadrispora)	2013-04-05 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEK	Rusty Tussock Moth  (L. Orgyia antiqua)	2013-04-05 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NBP	Post-Burn Mortality	2013-04-05 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFW	Swiss Needle Cast  (L. Phaeocryptopus gaumanni)	2012-11-19 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
UF	Unknown Fork Damage	2012-11-19 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PBC	Gray Mould  (L. Botrytris cinerea)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PCD	Neonectria radicicola  (L. Neonectria radicicola)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PCF	Seed or Cold Fungus  (L. Caloscypha fulgens)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PCP	Inland Spruce Cone Rust  (L. Chrysomyxa pirolata)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PDT	Cedar Leaf Blight  (L. Didymascella thujina)	1905-01-01 00:00:00	2010-12-31 00:00:00	2022-02-03 14:08:49
PFX	Fusarium spp.  (L. Fusarium spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PPG	Damping-off Disease  (L. Phoma glomerata)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PPX	Penicillium spp.  (L. Penicillium spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PSS	Sirococcus Blight  (L. Sirococcus strobilinus)	1905-01-01 00:00:00	2010-12-31 00:00:00	2022-02-03 14:08:49
PTX	Trichothecium spp.  (L. Trichothecium spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
T	Treatment Injuries	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TC	Chemical Injury	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TL	Logging Wounds	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TM	Other Mechanical Damage (non-logging)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TP	Planting (incorrectly planted)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TPM	Planting (poor microsite)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TR	Pruning Wound	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
TT	Thinning or Spacing Wound	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
V	Problem Vegetation	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
VH	Herbaceous Competition	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
VP	Vegetation Press	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
VS	Shrub Competition	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
VT	Tree Competition	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDL	Western Hemlock Looper  (L. Lambdina fiscellaria lugubrosa)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDM	Gypsy Moth  (L. Lymantria dispar)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDN	Birch Leaf Miners  (L. Fenusa pusilla and Profenusa thomsoni)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDP	Larch Sawfly  (L. Pristophora erichsoni)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDR	Alder Sawfly  (L. Eriocampa ovata)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDS	Balsam Fir Sawfly  (L. Neodiprion abietis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDT	Douglas-fir Tussock Moth  (L. Orgyia pseudotsugata)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDU	Satin Moth  (L. Leucoma salicis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDV	Variegated Cutworm  (L. Peridroma saucia)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDW	Western Spruce Budworm  (L. Choristoneura occidentalis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDX	Large Aspen Tortrix  (L. Choristoneura conflictana)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDZ	Western False Hemlock Looper  (L. Nepytia freemani)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IS	Shoot Insects	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISB	Western Cedar Borer  (L. Trachykele blondeli)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISE	European Pine Shoot Moth  (L. Rhyacionia buoliana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISG	Gouty Pitch Midge  (L. Cecidomyia piniinopsis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISP	Pitch Nodule Moths  (L. Petrova species)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISQ	Sequoia Pitch Moth  (L. Vespamima sequoiae)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ISS	Western Pine Shoot Borer  (L. Eucosma sonomana)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IW	Weevils	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWC	Conifer Seedling Weevil  (L. Steremnius carinatus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWM	Magdalis Species  (L. Magdalis spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWP	Lodgepole Pine Terminal Weevil  (L. Pissodes terminalis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWS	White Pine Weevil (on spruce)  (L. Pissodes strobi)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWW	Warrens Root Collar Weevil  (L. Hylobius warreni)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWY	Cylindrocopturus Weevil  (L. Cylindrocopturus spp.)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IWZ	Yosemite Bark Weevil  (L. Pissodes schwartzii)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
M	Mite Damage  (L. Trisetacus spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
N	Non-Biological (Abiotic) Injuries	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NB	Fire	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ND	Drought	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NF	Flooding	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NG	Frost	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NGC	Frost Crack	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NGH	Frost Heaved	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NGK	Shoot/Bud Frost Kill	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NH	Hail	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NK	Fumekill	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NL	Lightning	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NN	Road Salt	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NR	Redbelt	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NS	Slide	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NW	Windthrow	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NWS	Windthrow - Soil Failure	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NWT	Windthrow - Treatment or Harvest-related	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NY	Snow or Ice (includes snow press)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NZ	Sunscald	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
P	Cone and Seedling Fungal Pathogens	1997-08-13 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
PAX	Alternaria spp.  (L. Alternaria spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRL	Laminated Root Rot Fd form  (L. Inonotus sulphurascens)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRN	Annosus Root Disease  (L. Heterobasidion annosum)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRR	Rhizina Root Disease  (L. Rhizina undulata)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRT	Tomentosus Root Rot  (L. Inonotus tomentosus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DS	Stem Diseases  (L. Cankers and Rusts)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSA	Atropellis Canker  (L. Atropellis piniphila)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSB	White Pine Blister Rust  (L. Cronartium ribicola)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSC	Comandra Blister Rust  (L. Cronartium comandrae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSE	Sooty Bark Canker  (L. Encoelia pruinosa)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSG	Western Gall Rust  (L. Endocronartium harknessii)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSH	Hypoxylon Canker  (L. Entoleuca (Hypoxylon) mammatum)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSP	Cryptosphaeria Canker  (L. Cryptosphaeria populina)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSR	Ceratocystis Canker  (L. Ceratocystis fimbriata)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSS	Stalactiform Blister Rust  (L. Cronartium coleosporioides)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DST	Target Canker  (L. Nectria galligena)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSY	Cytospora Canker  (L. Cytospora chrysosperma)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
I	Insects	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IA	Aphids	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAB	Balsam Woolly Adelgid  (L. Adelges piceae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAC	Giant Conifer Aphid  (L. Cinara spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAG	Cooley Spruce Gall Adelgid  (L. Adelges cooleyi)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAL	Larch (Lw) Cone Woolly Aphid  (L. Adelges lariciatus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAS	Green Spruce Aphid  (L. Elatobium abietinum)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IB	Bark Beetles	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBB	Western Balsam Bark Beetle  (L. Dryocoetes confusus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBD	Douglas-fir Beetle  (L. Dendroctonus pseudotsugae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBI	Engraver Beetles  (L. Ips spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBM	Mountain Pine Beetle  (L. Dendroctonus ponderosae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBP	Twig Beetles  (L. Pityogenes or Pityophthorus spp.)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBS	Spruce Beetle  (L. Dendroctonus rufipennis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBT	Red Turpentine Beetle  (L. Dendroctonus valens)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IBW	Western Pine Beetle  (L. Dendroctonus brevicomis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
ID	Defoliators	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDA	Black Army Cutworm  (L. Actebia fennica)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDB	Two-Year Budworm  (L. Choristoneura biennis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDC	Larch Casebearer  (L. Coleophora laricella)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDD	Western Winter Moth  (L. Erannis tiliaria vancouverensis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDE	Eastern Spruce Budworm  (L. Choristoneura fumiferana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDF	Forest Tent Caterpillar  (L. Malacosoma disstria)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDG	Greenstriped Forest Looper  (L. Melanolophia imitata)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDH	Western Blackheaded Budworm  (L. Acleris gloverana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IDI	Pine Needle Sheath Miner  (L. Zellaria haimbachi)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CYT	Cedar (Cw) Cone Midge  (L. Mayetiola thujae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CYX	Seedworms  (L. Cydia spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
D	Diseases	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DB	Broom Rusts	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DBF	Fir Broom Rust  (L. Melampsorella caryophyllacearum)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DBS	Spruce Broom Rust  (L. Chrysomyxa arctostaphyli)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DD	Stem Decay	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDB	Birch Trunk Rot  (L. Fomes fomentarius)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDD	Sulfur Fungus  (L. Laetiporus sulphureus)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDE	Rust Red Stringy Rot  (L. Echindontium tinctorium)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDF	Brown Crumbly Rot  (L. Fomitopsis pinicola)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDH	Hardwood Trunk Rot  (L. Phellinus ignarius)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDO	Cedar Brown Pocket Rot  (L. Poria sericeomollis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDP	Red Ring Rot  (L. Phellinus pini)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDQ	Quinine Conk Rot  (L. Fomitopsis officinalis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDS	Schweinitzii Butt Rot  (L. Phaeolus schweinitzii)	1905-01-01 00:00:00	2010-12-31 00:00:00	2022-02-03 14:08:49
DDT	Aspen Trunk Rot  (L. Phellinus tremulae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DF	Foliage Diseases	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFA	Western Pine Aster Rust  (L. Coleosporium asterum)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFC	Large-Spored Spruce-Labrador Tea Rust  (L. Chrysomyxa ledicola)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFD	Spruce Needle Cast  (L. Lirula macrospora)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFE	Elytroderma Needle Cast  (L. Elytroderma deformans)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFH	Larch Needle Blight  (L. Hypodermella laricis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFL	Pine Needle Cast  (L. Lophodermella concolor)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFM	Larch Needle Cast  (L. Meria laricis)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFP	Fir Fireweed Rust  (L. Pucciniastrum epilobi)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFR	Douglas-fir Needle Cast  (L. Rhabdocline pseudotsugae)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DFS	Dothistroma Needle Blight  (L. Dothistroma septosporum)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DL	Disease Caused Dieback	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLD	Dermea Canker  (L. Dermea pseudotsugae)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLF	Red Flag Disease  (L. Potebniamyces balsamicola)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLP	Phomopsis Canker  (L. Phomopsis lokoyae)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLS	Sydowia (Sclerophoma) Tip Dieback  (L. Sclerophoma pithyophila)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLV	Aspen-Poplar Twig Blight  (L. Venturia spp.)	1999-04-07 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DM	Dwarf Mistletoe	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DMF	Douglas-fir Dwarf Mistletoe  (L. Arceuthobium douglasii)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DMH	Hemlock Dwarf Mistletoe  (L. Arceuthobium tsugense)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DML	Larch Dwarf Mistletoe  (L. Arceuthobium laricis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DMP	Lodgepole pine Dwarf Mistletoe  (L. Arceuthobium americanum)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DR	Root Disease	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRA	Armillaria Root Disease  (L. Armillaria ostoyae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRB	Black Stain Root Disease  (L. Leptographium wageneri)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DRC	Laminated Root Rot cedar strain  (L. Phellinus weirii)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
C	Cone and Seed Insects	1997-08-13 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CAH	Cone Resin Midge  (L. Asynapta hopkinsi)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CBC	Fir (Fd) Cone Moth  (L. Barbara colfaxiana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CBX	Fir Cone Moth  (L. Barbara spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CCP	Douglas-fir Cone Scale Midge  (L. Camptomyia pseudotsugae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CDC	Spruce (Sx) Cone Gall Midge  (L. Kaltenbachiola (Dasineura) canadensis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CDD	Fir Seed Midge  (L. Kaltenbachiola (Dasineura) abiesemia)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CDR	Spruce (Sx) Cone Axis Midge  (L. Kaltenbachiola (Dasineura) rachiphaga)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CDX	Kaltenbachiola Midges  (L. Kaltenbachiola (Dasineura) spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CEA	Fir Seed Maggot  (L. Earomyia abietum)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CEB	Spruce Cone Maggot  (L. Earomyia barbara)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CEQ	Earomyia aquilonia  (L. Earomyia aquilonia)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CEX	Earomyia Maggots  (L. Earomyia spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CFP	Fir (Fd) Cone Beetle  (L. Ernobius punctulatus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CHX	Budworms  (L. Choristoneura spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIA	Fir Coneworm  (L. Dioryctria abietivorella)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIP	Fir (Fd) Coneworm  (L. Dioryctria pseudotsugella)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIR	Spruce (Sx) Coneworm  (L. Dioryctria reniculelloides)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIS	Pine (Py)  Coneworm  (L. Dioryctria rossi)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIV	Ponderosa Pine  (Py) Coneworm  (L. Dioryctria auranticella)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CIX	Coneworms  (L. Dioryctria spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CLO	Western Conifer Seed Bug  (L. Leptoglossus occidentalis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMA	Ponderosa Pine (Py) Seed Chalcid  (L. Megastigmus albifrons)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMC	Spruce (Sx) Seed Chalcid  (L. Megastigmus piceae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CML	Subalpine Fir (Bl) Seed Chalcid  (L. Megastigmus lasiocarpae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMP	Fir Seed Chalcid  (L. Megastigmus pinus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMR	Megastigmus rafni  (L. Megastigmus rafni)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMS	Fir (Fd) Seed Chalcid  (L. Megastigmus spermotrophus)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMT	Hemlock Seed Chalcid  (L. Megastigmus tsugae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CMX	Seed Chalcids  (L. Megastigmus spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CNP	Pine Cone Beetle  (L. Conophthorus ponderosae)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CPS	Spruce Gall Adelgid  (L. Pineus similis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CRX	Cone Scale Midges  (L. Resseliella spp.)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CSN	Spiral Spruce Cone Borer  (L. Strobilomyia neanthracina)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CTO	Fir (Fd) Cone Gall Midge  (L. Contarinia oregonensis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CTW	Fir (Fd) Cone Scale Midge  (L. Contarinia washingtonensis)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CVP	White Pine (Pw) Cone Borer  (L. Eucosma ponderosa)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CVR	Lodgepole Pine (Pl) Cone Borer  (L. Eucosma recissoriana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CYC	Spruce (Sx) Seed Midge  (L. Mayetiola carpophaga)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CYP	Ponderosa Pine (Py) Seedworm  (L. Cydia piperana)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
CYS	Spruce (Sx) Seedworm  (L. Cydia strobilella)	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
A	Animal Damage	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AB	Bear	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AC	Cattle	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AD	Deer	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AE	Elk	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AH	Hare or Rabbit	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AM	Moose	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AP	Porcupine	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AS	Squirrel	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AV	Vole	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AX	Birds	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AZ	Beaver	1905-01-01 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
O	Tree damage checked for but NONE exists	2000-01-26 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
U	Tree damage present but specific damage agent unknown	2000-01-26 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IEL	Pine Needleminer  (L. Coletechnites spp.)	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NCF	Douglas-fir Decline	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NE	Cedar Flagging	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NI	Redheart	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDR	Red Heart Rot  (L. Stereum sanguinolentum)	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DDU	Stringy Butt Rot  (L. Perenniporia subacida)	2014-07-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
IAP	Pine Needle Scale  (L. Chionaspis (Phenacaspis) pinifoliae)	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NDM	Drought - Mortality	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NDF	Drought - Foliage Loss/Damage	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
AN	Horse	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLC	Lodgepole Pine Dieback (Cenangium ferruginosum)	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DSN	Aspen Running Canker (Neodothiora populina)	2022-01-24 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NYP	Snow Press	2020-04-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
NYB	Snow or Ice Breakage	2020-04-29 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
DLT	Sirococcus Tip Blight  (L. Sirococcus conigenus)	2020-10-08 00:00:00	9999-12-31 00:00:00	2022-02-03 14:08:49
\.
